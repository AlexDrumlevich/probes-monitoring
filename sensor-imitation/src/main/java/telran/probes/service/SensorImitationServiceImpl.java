package telran.probes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.ApplicationContext;

import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.ProbeData;
@Service
@RequiredArgsConstructor
@Slf4j
public class SensorImitationServiceImpl implements SensorImitationService {

	@NonNull
	private ApplicationContext context;
	@NonNull
	private StreamBridge streamBridge;

	private String bindingOutstreaName = "consumerProbeData-out-0";
	@Override
	public void runSensor(long id, float minRangeValue, float maxRandeValue, int deviationProbabilityPercentage,
			float maxDeviationSize, int sheduledTimeRateMs) {

		Scheduler scheduler = context.getBean("Scheduler", Scheduler.class);
		scheduler.setSheduledTimeRateMs(sheduledTimeRateMs);
		scheduler.setMethod(() -> {
					ProbeData probeData = getRandomProbeData(id, minRangeValue, maxRandeValue, deviationProbabilityPercentage,maxDeviationSize);
					log.trace("Sending probe data: {}", probeData);
					streamBridge.send(bindingOutstreaName, probeData);
				});
				scheduler.runSchedulerMethod();

	}

	ProbeData getRandomProbeData(long sensorId, float minRangeValue, float maxRangeValue, int deviationProbabilityPercentage, float maxDeviationSize) {

		float resultValue;

		deviationProbabilityPercentage = deviationProbabilityPercentage > 100 ? 100 : deviationProbabilityPercentage;

		boolean isDeviationValue = deviationProbabilityPercentage > 0 && (((int) Math.random() * 100 + 1) <= deviationProbabilityPercentage);  

		if(isDeviationValue) {
			resultValue = ((float)Math.random() * (maxRangeValue + maxDeviationSize - maxRangeValue)) + maxRangeValue;
		} else {
			resultValue = ((float)Math.random() * (maxRangeValue - minRangeValue)) + minRangeValue;
		}

		return new ProbeData(sensorId, resultValue, System.currentTimeMillis());




	}

}
