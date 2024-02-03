package telran.probes.service;

import telran.model.Sensor;

public interface SensorImitationService {

	void runSensors();
	void addSensorToScheduler(Sensor sensor);
	void addSensor(long id);
	void updateSensor(long id);
	void saveSensor(Sensor sensor);
}
