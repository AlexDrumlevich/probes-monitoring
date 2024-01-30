package telran.probes;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import telran.probes.service.SensorImitationService;


@SpringBootApplication
@EnableScheduling
public class SensorImitation implements ApplicationRunner {

	@Autowired
	SensorImitationService sensorImitationService;
	@Value("${app.sensors}")
	String sensorsConfig;




	public static void main(String[] args) {
		SpringApplication.run(SensorImitation.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.out.println("********************************");
		
		Arrays.stream(sensorsConfig.split(",")).forEach(s -> {

			String[] sensorConfig = s.split(" ");
			sensorImitationService.runSensor(
					Long.valueOf(sensorConfig[0]),
					Float.valueOf(sensorConfig[1]),
					Float.valueOf(sensorConfig[2]),
					Integer.valueOf(sensorConfig[3]),
					Float.valueOf(sensorConfig[4]),
					Integer.valueOf(sensorConfig[5])
					);
		});

	}




}
