package telran.avg_populator;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import telran.avg_populator.service.AvgPopulatorService;
import telran.probes.dto.ProbeData;

@SpringBootApplication
public class AvgPopulatorAppl {
	
	@Autowired
	AvgPopulatorService avgPopulatorService;
	
	public static void main(String[] args) {
		SpringApplication.run(AvgPopulatorAppl.class, args);
	}
	
	@Bean
	Consumer<ProbeData> avgPopulating(){
		return avgPopulatorService::saveProbeData;
	}
	
	
}
