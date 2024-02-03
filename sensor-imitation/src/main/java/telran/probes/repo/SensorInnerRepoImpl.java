package telran.probes.repo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.validation.constraints.AssertFalse.List;
import telran.model.Sensor;




public class SensorInnerRepoImpl implements SensorInnerRepo {
	
	private Map<Long, Sensor> sensorsInnerMap = new HashMap<>();  
	@Autowired
	SensorOuterRepo sensorOuterRepo;
	
	
	@Override
	public void setAllFromDB() {
		sensorOuterRepo.findAll().forEach(s -> setSensorIntoInnerMap(s));
	}
	
	@Override
	public void addUpdate(Long id) {
		sensorOuterRepo.findById(id).ifPresentOrElse(
				s -> sensorsInnerMap.put(id, s),
				() -> {throw new IllegalStateException(String.format("Nothing was foud in DB by id: %d (func update)", id.longValue()));}
			);
	}
	
	public void setSensorIntoInnerMap(Sensor sensor) {
		sensorsInnerMap.put(sensor.getId(), sensor);
	}
	public Sensor getSensorFromInnerMap(long id) {
		return sensorsInnerMap.get(id);
	}
	
	public Collection<Sensor> getSensors() {
		return sensorsInnerMap.values();
	}




	
	
	
	
	
}
