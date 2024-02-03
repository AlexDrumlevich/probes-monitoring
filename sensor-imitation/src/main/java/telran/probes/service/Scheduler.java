package telran.probes.service;


import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Consumer;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.Setter;
import telran.model.Sensor;

@Component("Scheduler")
public class Scheduler {

	private TreeMap<Long, Set<Sensor>> sensorsScheduled = new TreeMap<>();




	void addSensorIntoScheduler(Sensor sensor) {
		sensorsScheduled.computeIfAbsent(System.currentTimeMillis() + sensor.getSignalTimeRate(), k -> new HashSet<>()).add(sensor);
	}

	@Setter
	private Consumer<Sensor> method;


	@Scheduled(fixedRateString ="${app.scheduler.fixedRate}")
	void runSchedulerMethod() {


		long currentTime = System.currentTimeMillis();

		Set<Entry<Long, Set<Sensor>>> entriesForExecuting = sensorsScheduled.subMap(0l, currentTime).entrySet();

		if(!entriesForExecuting.isEmpty()) {
			Set<Long> keysForRemoving = new HashSet<>();
			Set<Sensor> sensorsForScheduling = new HashSet<>();
			
			sensorsScheduled.subMap(0l, currentTime).entrySet().forEach(keyValue -> {
				keyValue.getValue().stream().forEach(s -> {
					method.accept(s);
					sensorsForScheduling.add(s);
				});
				keysForRemoving.add(keyValue.getKey());
				
			});
			keysForRemoving.stream().forEach(k -> sensorsScheduled.remove(k));
			sensorsForScheduling.stream().forEach(s -> addSensorIntoScheduler(s));
		}
	}
}
