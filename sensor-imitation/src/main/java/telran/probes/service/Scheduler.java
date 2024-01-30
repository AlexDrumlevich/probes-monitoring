package telran.probes.service;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import lombok.Setter;

@Component("Scheduler")
@Scope(value="prototype")

public class Scheduler {

	@Setter
	private long sheduledTimeRateMs = 1000;
	@Setter
	private Runnable method;
	
	
	@Bean("TimeRate")
	@Scope(value="prototype")
	private String getSheduledTimeRateMsString() {
		return String.valueOf(sheduledTimeRateMs);
	}

	
	//@Scheduled(fixedRateString = "#{TimeRate}")
	//@Scheduled(fixedRateString ="#{Scheduler.getFixedRateMs()}")
	@Scheduled(fixedRate = 1000)
	void runSchedulerMethod() {
		method.run();
		
	
	}
	
}
