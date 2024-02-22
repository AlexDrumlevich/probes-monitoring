package telran.avg_reducer;

import java.util.Optional;
import java.util.function.Consumer;

import javax.xml.crypto.Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplication.Running;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import lombok.extern.slf4j.Slf4j;
import telran.avg_reducer.repo.ProbesListRepo;
import telran.avg_reducer.service.AvgReducerService;
import telran.probes.dto.ProbeDataDto;

@SpringBootApplication
@ComponentScan("telran")
@Slf4j
public class AvgReducerApp implements ApplicationRunner {

	@Autowired
	private AvgReducerService avgReducerService;
	@Autowired
	private ProbesListRepo repo;
	@Autowired
	private StreamBridge streamBridge;

	
	@Value("${app.binding_name_reduced_data_out}")
	String bindingNameAvgSending;

	public static void main(String[] args) {
		SpringApplication.run(AvgReducerApp.class, args);
		
	}

	
	@Bean
	public Consumer<ProbeDataDto> avg() {
		return data -> {
			log.trace("AvgReducerApp Consumer got data from Message broker: {}", data);
			Optional.ofNullable(avgReducerService.getAvgValue(data)).ifPresentOrElse(
					pdoduceAvgProdeData(data),
					() -> log.trace("AvgReducerApp Consumer got null from service"));

		};
	}

	private Consumer<Long> pdoduceAvgProdeData(ProbeDataDto data) {
		return value -> 
			{
				ProbeDataDto dataToSend = new ProbeDataDto(
					data.sensorId(),
					value,
					System.currentTimeMillis()
				);
				streamBridge.send(bindingNameAvgSending, dataToSend);
				log.trace("AvgReducerApp Consumer got value from service: {} and sent by binding name: {},  data to Message broker: {}", value, bindingNameAvgSending, dataToSend);
			};
	}


	@Override
	public void run(ApplicationArguments args) throws Exception {
		for(int i = 0; i < 30; i++) {
		log.debug("Calling getAvgValue from service ......");	
		avgReducerService.getAvgValue(new ProbeDataDto(1, 99, 3));
		}
	}


}
