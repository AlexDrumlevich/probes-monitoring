package telran.avg_reducer.service;

import telran.probes.dto.ProbeData;


public interface AvgReducerService {

	Long getAvgValue(ProbeData probeData);
	
}
