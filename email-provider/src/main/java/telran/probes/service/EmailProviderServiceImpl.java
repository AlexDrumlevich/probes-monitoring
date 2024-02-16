package telran.probes.service;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import telran.exceptions.NotFoundException;
import telran.probes.dto.SensorEmailsDto;
import telran.probes.dto.SensorRangeDto;
import telran.probes.repository.EmailProviderRepository;

@Slf4j
@Service
public class EmailProviderServiceImpl implements EmailProviderService {
@Autowired
private EmailProviderRepository emailProviderRepository;



	@Override
	public SensorEmailsDto findSensorEmail(long id) {
		SensorEmailsDto result = emailProviderRepository.findSensorById(id).orElseThrow(() -> new NotFoundException(String.format("Sensor id %s not found", id)));
		log.debug("EmailProviderRepository findSensor by id: {} got {}", id, result);
		return result;
	}

}
