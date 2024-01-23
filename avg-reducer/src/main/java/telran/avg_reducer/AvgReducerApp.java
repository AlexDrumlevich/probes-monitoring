package telran.avg_reducer;

import java.util.Optional;
import java.util.function.Consumer;

import javax.xml.crypto.Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;

import lombok.extern.slf4j.Slf4j;
import telran.avg_reducer.service.AvgReducerService;
import telran.probes.dto.ProbeData;

@SpringBootApplication
@Slf4j
public class AvgReducerApp {

	@Autowired
	private AvgReducerService avgReducerService;
	@Autowired
	private StreamBridge streamBridge;

	
	@Value("${app.avg.binding.name}")
	String bindingNameAvgSending;

	public static void main(String[] args) {
		SpringApplication.run(AvgReducerApp.class, args);

	}

	@Bean
	public Consumer<ProbeData> avg() {
		return data -> {
			log.trace("AvgReducerApp Consumer got data from Message broker: {}", data);
			Optional.ofNullable(avgReducerService.getAvgValue(data)).ifPresentOrElse(
					pdoduceAvgProdeData(data),
					() -> log.trace("AvgReducerApp Consumer got null from service"));

		};
	}

	private Consumer<Long> pdoduceAvgProdeData(ProbeData data) {
		return value -> 
			{
				ProbeData dataToSend = new ProbeData(
					data.sensorId(),
					value,
					System.currentTimeMillis()
				);
				streamBridge.send(bindingNameAvgSending, dataToSend);
				log.trace("AvgReducerApp Consumer got value from service: {} and sent by binding name: {},  data to Message broker: {}", value, bindingNameAvgSending, dataToSend);
			};
	}
}
