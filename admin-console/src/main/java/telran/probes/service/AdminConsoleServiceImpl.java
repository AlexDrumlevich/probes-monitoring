package telran.probes.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.exceptions.NotFoundException;
import telran.model.Sensor;
import telran.probes.dto.SensorDto;
import telran.probes.dto.SensorEmailsDto;
import telran.probes.dto.SensorRangeDto;
import telran.probes.repo.AdminConsoleRepo;


@Service
@Slf4j
@RequiredArgsConstructor
public class AdminConsoleServiceImpl implements AdminConsoleService {

	@NonNull
	AdminConsoleRepo repo;
	@NonNull
	StreamBridge streamBridge;
	
	@Value("${app.binding_name_sensor_event_out}")
	String bindingNameString;
	
	@Value("${app.event.delimeter}")
	String delimeter;
		
	@Value("${app.update.token.range}")
	String updateRangeToken;
	
	@Value("${app.update.token.email}")
	String updateEmailToken;
	
	
	@Override
	public SensorRangeDto updateSensorRange(SensorRangeDto sensorRange) {

		if(sensorRange.maxValue() < sensorRange.minValue()) {
			log.error("Func updateSensorRange: max range value: {} less than min range value: {}", sensorRange.maxValue(), sensorRange.minValue());
			throw new IllegalRangeException(String.format("max range value: %d less than min range value: %d", sensorRange.maxValue(), sensorRange.minValue()));
		}
		SensorDto sensorDto = repo.findById(sensorRange.id()).orElseThrow( 
				() -> {
					log.debug("Func updateSensorRange: sensor by id: {} has not found", sensorRange.id());
					throw new NotFoundException(String.format("sensor by id: %d has not found", sensorRange.id()));
				}
				).toDto();
		repo.save(Sensor.of(
				new SensorDto(
						sensorDto.id(),
						sensorRange.minValue(),
						sensorRange.maxValue(),
						sensorDto.signalTimeRate(),
						sensorDto.emails()
				)
			)
		);
		streamBridge.send(bindingNameString, String.format("%s%s%d", updateRangeToken, delimeter, sensorRange.id()));
		return sensorRange;
	}

	@Override
	public SensorEmailsDto updateSensorEmails(SensorEmailsDto sensorEmails) {
		SensorDto sensorDto = repo.findById(sensorEmails.id()).orElseThrow( 
				() -> {
					log.debug("Func updateSensorRange: sensor by id: {} has not found", sensorEmails.id());
					throw new NotFoundException(String.format("sensor by id: %d has not found", sensorEmails.id()));
				}
				).toDto();
		repo.save(Sensor.of(
				new SensorDto(
						sensorDto.id(),
						sensorDto.minValue(),
						sensorDto.maxValue(),
						sensorDto.signalTimeRate(),
						sensorEmails.emails()
				)
			)
		);
		streamBridge.send(bindingNameString, String.format("%s%s%d", updateEmailToken, delimeter, sensorEmails.id()));
		return sensorEmails;
	}

	@Override
	public SensorDto addSensor(SensorDto sensor) {
		repo.save(Sensor.of(sensor));
		streamBridge.send(bindingNameString, String.format("%s%s%d", "add", delimeter, sensor.id()));
		return sensor;
	}

}


