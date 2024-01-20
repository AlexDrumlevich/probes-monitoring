package telran.sensors.service;

import telran.sensors.dto.SensorRangeDto;

public interface SensorRangeProviderService {

	SensorRangeDto findSensor(String id);
	
}
