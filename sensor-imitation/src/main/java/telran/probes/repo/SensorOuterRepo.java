package telran.probes.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import telran.model.Sensor;

public interface SensorOuterRepo extends MongoRepository<Sensor, Long> {

}
