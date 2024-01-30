package telran.probes.service;

public interface SensorImitationService {

	void runSensor(long id, float minRangeValue, float maxRandeValue, int deviationProbabilityPercentage, float maxDeviationSize, int sheduledTimeRateMs);
}
