package telran.probes;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;import java.util.function.LongFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.extern.slf4j.Slf4j;
import telran.model.Sensor;

import telran.probes.repo.SensorRepo;
import telran.probes.service.SensorImitationService;


@SpringBootApplication
@EnableScheduling
@Slf4j
public class SensorImitation implements ApplicationRunner {

	@Autowired
	SensorImitationService sensorImitationService;
	@Autowired
	SensorRepo sensorRepo;
	
	@Value("${app.sensors}")
	String sensorsConfig;
	
	@Value("app.event.delimeter")
	String delimeter;




	public static void main(String[] args) {
		SpringApplication.run(SensorImitation.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
	
		Iterable<Sensor> sensIterable = sensorRepo.findAll();
		
		if(!sensIterable.iterator().hasNext()) {
			
			Arrays.stream(sensorsConfig.split(",")).forEach(s -> {

				String[] sensorConfig = s.split(" ");
				
				sensorRepo.save(new Sensor (
						Long.valueOf(sensorConfig[0]),
						Double.valueOf(sensorConfig[1]),
						Double.valueOf(sensorConfig[2]),
						Integer.valueOf(sensorConfig[3]),				
						List.of(sensorConfig[4])
						)
				);
				
			});
			
		}
		
		sensorImitationService.runSensors();
	}

	
	@Bean
	Consumer<String> sensorEvent() {
		return (s) -> {
			log.debug("Consumer sensor event got data: {}", s);
			String[] data = s.split(delimeter);
			String action = data[0];
			Long id;
			try {
				id = Long.valueOf(data[1]);
			} catch (Exception e) {
				log.error("Sensor event consumer got illegal value as id: {}", data[1]);
				throw new IllegalArgumentException(String.format("Sensor event consumer got illegal value as id: %s", data[1]));
			}
			
			switch (action) {
			case "add":
				sensorImitationService.addSensor(id);
				break;
			case "update":
				sensorImitationService.updateSensor(id);
				break;
			default:
				log.error("Sensor event consumer got illegal action value: {}", action);
				throw new IllegalArgumentException(String.format("Sensor event consumer got unexpected value: %s", action));
			}
		};
	}



}
