package telran.avg_reducer.service;



import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.avg_reducer.repo.ProbesListRepo;
import telran.model.ProbesList;
import telran.probes.dto.ProbeData;


import java.util.*;
@Slf4j
@Service
@RequiredArgsConstructor
public class AvgReducerServiceImpl implements AvgReducerService {
final ProbesListRepo probesListRepo;
@Value("${app.average.reducing.size}")
int reducingSize;
	@Override
	@Transactional
	public Long getAvgValue(ProbeData probeData) {
		long sensorId = probeData.sensorId();
		Long res = null;
		ProbesList probesList = probesListRepo.findById(sensorId).orElse(null);
		if (probesList == null) {
			probesList = new ProbesList(sensorId);
			log.debug("probesList for sensor {} doesn't exist", sensorId);
		}
		List<Float> values = probesList.getValues();
		values.add(probeData.value());
		if(values.size() == reducingSize) {
			log.debug("reducing for sensor {}",sensorId);
			res = (long) values.stream().mapToLong(v -> v.longValue())
					.average().orElse(0);
			values.clear();
		}
		probesListRepo.save(probesList);
		log.debug("saved probesList {}", probesList);

		return res;
	}

}
