package telran.probes;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.*;

import telran.probes.service.SensorRangeProviderService;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class AnalyzerAppl {
final SensorRangeProviderService providerService;
final StreamBridge streamBridge;

@Value("${app.binding_name_sensor_deviation_out}")
String deviationBindingName;

	public static void main(String[] args) {
		SpringApplication.run(AnalyzerAppl.class, args);

	}
	
	@Bean(name = "consumerProbeData")
	public Consumer<ProbeDataDto> consumerProbeData() {
		return this::consumeMethod;
	}
	void consumeMethod(ProbeDataDto probeData) {
		log.trace("received probe: {}", probeData);
		long sensorId = probeData.sensorId();
		SensorRangeDto range = providerService.getSensorRange(sensorId);
		double value = probeData.value();
		
		Double border = Double.NaN;
		if (value < range.minValue()) {
			border = range.minValue();
		} else if(value > range.maxValue()) {
			border = range.maxValue();
		}
		if (!border.isNaN()) {
			double deviation = value - border;
			log.debug("deviation: {}", deviation);
			ProbeDataDeviationDto dataDeviation =
					new ProbeDataDeviationDto(sensorId, value, deviation, System.currentTimeMillis());
			streamBridge.send(deviationBindingName, dataDeviation);
			log.debug("deviation data {} sent to {}", dataDeviation, deviationBindingName);
			
		}
	}
	

}
