package telran.sensors.service;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import telran.exceptions.NotFoundException;
import telran.probes.dto.SensorRangeDto;
import telran.sensors.repository.SensorRepository;
@Slf4j
@Service
public class SensorRangeProviderServiceImpl implements SensorRangeProviderService {
@Autowired
private SensorRepository sensorRepository;
@Autowired
private Validator validator;

	@Override
	public SensorRangeDto findSensorRange(long id) {
		// TODO Auto-generated method stub
		SensorRangeDto result = sensorRepository.findSensorById(id).orElseThrow(() -> new NotFoundException(String.format("Sensor id %s not found", id)));
		log.debug("SensorRepository findSensor by id: {} got {}", id, result);
		// Set<ConstraintViolation<SensorRangeDto>> constraintViolations = validator.validate(new SensorRangeDto(null, null, null));
		System.out.println("*************************");
		// constraintViolations.stream().forEach(v -> System.out.println(v.getMessage()));
			System.out.println("*************************");
		return result;
	}

}
