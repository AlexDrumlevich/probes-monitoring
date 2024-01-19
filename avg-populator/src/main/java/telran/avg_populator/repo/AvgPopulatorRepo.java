package telran.avg_populator.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import telran.avg_populator.model.ProbeDataDoc;

public interface AvgPopulatorRepo extends MongoRepository<ProbeDataDoc, Long> {

}
