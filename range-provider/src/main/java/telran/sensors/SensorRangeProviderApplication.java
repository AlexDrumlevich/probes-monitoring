package telran.sensors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = "telran")
public class SensorRangeProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(SensorRangeProviderApplication.class, args);
	}

}
