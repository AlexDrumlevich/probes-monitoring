package telran.probes.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import lombok.Getter;

@Configuration
@Getter
public class SensorEmailProviderConfiguration {
	@Value("${app.sensor.range.provider.host:localhost}")
	String host;
	@Value("${app.sensor.range.provider.port:8282}")
	int port;
	@Value("${app.sensor.range.provider.url:/sensor/range}")
	String url;
	
	@Bean
	RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
}