package telran.probes.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import lombok.Getter;

@Configuration
@Getter
public class SensorRangeProviderConfiguration {
	@Value("${app.sensor.range.provider.host:localhost}")
	String host;
	@Value("${app.sensor.range.provider.port}")
	int port;
	@Value("${app.sensor.range.provider.url}")
	String url;
	@Value("${app.sensor.range.provider.default.min}")
	double minDefaultValue;
	@Value("${app.sensor.range.provider.default.max}")
	double maxDefaultValue;
	@Bean
	RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
}