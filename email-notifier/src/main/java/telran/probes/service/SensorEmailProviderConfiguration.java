package telran.probes.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import lombok.Getter;

@Configuration
@Getter
public class SensorEmailProviderConfiguration {
	@Value("${app.sensor.emails.provider.host}")
	String host;
	@Value("${app.sensor.emails.provider.port}")
	int port;
	@Value("${app.sensor.emails.provider.url}")
	String url;
	@Value("${app.emails.provider.default}")
	String [] emails;
	@Bean
	RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
}