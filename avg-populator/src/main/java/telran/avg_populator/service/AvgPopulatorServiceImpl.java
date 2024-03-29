package telran.avg_populator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import telran.avg_populator.repo.AvgPopulatorRepo;
import telran.model.ProbeDataDoc;
import telran.probes.dto.ProbeDataDto;

@Slf4j
@Service
public class AvgPopulatorServiceImpl implements AvgPopulatorService {
	@Autowired
	AvgPopulatorRepo avgPopulatorRepo;
	
	@Override
	public void saveProbeData(ProbeDataDto probeData) {
		log.trace("Service got ProbeData: {}", probeData);
		ProbeDataDto savedProbeData = avgPopulatorRepo.save(ProbeDataDoc.of(probeData)).toDto();
		log.trace("Service saved ProbeData: {}", savedProbeData);

	}

}
