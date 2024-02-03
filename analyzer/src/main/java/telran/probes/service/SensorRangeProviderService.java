package telran.probes.service;

import telran.probes.dto.SensorRangeDto;

public interface SensorRangeProviderService {
SensorRangeDto getSensorRange(long sensorId);
}
