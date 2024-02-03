package telran.sensors.service;

import telran.probes.dto.SensorRangeDto;

public interface SensorRangeProviderService {

	SensorRangeDto findSensorRange(long id);
	
}
