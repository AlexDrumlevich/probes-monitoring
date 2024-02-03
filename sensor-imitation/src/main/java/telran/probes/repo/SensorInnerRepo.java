package telran.probes.repo;

import java.util.Collection;

import telran.model.Sensor;




public interface SensorInnerRepo {
	
	void setAllFromDB();
	void addUpdate(Long id);
	void setSensorIntoInnerMap(Sensor sensor);
	Sensor getSensorFromInnerMap(long id);
	public Collection<Sensor> getSensors();
	
}
