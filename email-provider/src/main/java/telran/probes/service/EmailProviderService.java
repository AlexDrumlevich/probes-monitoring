package telran.probes.service;

import telran.probes.dto.SensorEmailsDto;


public interface EmailProviderService {

	SensorEmailsDto findSensorEmail(long id);
	
}
