package telran.avg_reducer.service;

import telran.probes.dto.ProbeDataDto;


public interface AvgReducerService {

	Long getAvgValue(ProbeDataDto probeData);
	
}
