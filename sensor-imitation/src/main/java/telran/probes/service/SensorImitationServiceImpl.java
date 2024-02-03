package telran.probes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.ApplicationContext;

import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.ProbeDataDto;
import telran.model.Sensor;
import telran.probes.repo.SensorRepo;
@Service
@RequiredArgsConstructor
@Slf4j
public class SensorImitationServiceImpl implements SensorImitationService {

	@NonNull
	private ApplicationContext context;
	@NonNull
	private StreamBridge streamBridge;
	@NonNull
	private Scheduler scheduler;
	@NonNull
	private SensorRepo sensorRepo;
	@Value("${app.sensor.deviationProbability}")
	int deviationProbability;
	@Value("${app.sensor.deviationSize}")
	int deviationSize;
	
	@Value("${app.binding_name.output}")
	private String bindingOutstreaName;

	
	@Override
	public void runSensors() {
		sensorRepo.setAllFromDB();
		sensorRepo.getSensors().forEach(s -> scheduler.addSensorIntoScheduler(s));
		scheduler.runSchedulerMethod();
	

		
		scheduler.setMethod((sensor) -> {
					ProbeDataDto probeData = getRandomProbeData(sensor.getId(), sensor.getMinValue(), sensor.getMaxValue(), deviationProbability, deviationSize);
					log.trace("Sending probe data: {}", probeData);
					streamBridge.send(bindingOutstreaName, probeData);
				});	
	}

	private ProbeDataDto getRandomProbeData(long sensorId, double minRangeValue, double maxRangeValue, int deviationProbability, int deviationSize) {

		double resultValue;

		deviationProbability = deviationProbability > 100 ? 100 : deviationProbability;

		boolean isDeviationValue = deviationProbability > 0 && (((int) Math.random() * 100 + 1) <= deviationProbability);  

		if(isDeviationValue) {
			double deviation = (maxRangeValue - minRangeValue) * (Math.random() * deviationSize / 100);
			resultValue = Math.random() > 0.5 ? minRangeValue - deviation : maxRangeValue + deviation;
	
		} else {
			resultValue = (Math.random() * (maxRangeValue - minRangeValue)) + minRangeValue;
		}

		return new ProbeDataDto(sensorId, (float) resultValue, System.currentTimeMillis());

	}
	
	@Override
	public void addSensorToScheduler(Sensor sensor) {
		scheduler.addSensorIntoScheduler(sensor);
	}


	@Override
	public void addSensor(long id) {
		sensorRepo.addUpdate(id);
		addSensorToScheduler(sensorRepo.getSensorFromInnerMap(id));
	}


	@Override
	public void updateSensor(long id) {
		sensorRepo.addUpdate(id);
	}

	@Override
	public void saveSensor(Sensor sensor) {
		sensorRepo.save(sensor);
		
	}

	
	
	
}
