package telran.probes.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import lombok.Getter;

@Configuration
@Getter
public class AccountProviderConfiguration {

	@Value("${app.sensor.account.provider.host}")
	String host;
	@Value("${app.sensor.account.provider.port}")
	int port;
	@Value("${app.sensor.account.provider.url}")
	String url;
	@Bean
	RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

}