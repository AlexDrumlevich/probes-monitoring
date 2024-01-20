package telran.avg_populator.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import telran.model.ProbeDataDoc;



public interface AvgPopulatorRepo extends MongoRepository<ProbeDataDoc, Long> {

}
