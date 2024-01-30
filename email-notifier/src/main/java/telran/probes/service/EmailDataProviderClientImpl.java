package telran.probes.service;


import java.util.*;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.web.client.RestTemplate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.SensorEmails;
import telran.probes.dto.SensorRange;
@Service
@RequiredArgsConstructor
@Slf4j
@Configuration
public class EmailDataProviderClientImpl implements EmailDataProviderClient {
	@Getter
	HashMap<Long, String[]> mapSensorEmails = new HashMap<>();
	@Value("${app.update.message.delimiter:#}")
	String delimiter;
	@Value("${app.update.token.range:email-update}")
	String emailUpdateToken;
	final SensorEmailProviderConfiguration providerConfiguration;
	final RestTemplate restTemplate;
	@Override
	public String[] getEmails(long sensorId) {
		String[] sensorEmails = mapSensorEmails.get(sensorId);
		
		return sensorEmails == null ? getSensorEmailsFromService(sensorId) : sensorEmails;
	}
	
	@Bean(name = "ConfigChangeConsumer")
	Consumer<String> configChangeConsumer() {
		return this::checkConfigurationUpdate;
	}
	void checkConfigurationUpdate(String message) {
		
		String [] tokens = message.split(delimiter);
		if(tokens[0].equals(emailUpdateToken)) {
			updateMapRanges(tokens[1]);
		}
	}
	private void updateMapRanges(String sensorIdStr) {
		long id = Long.parseLong(sensorIdStr);
		if (mapSensorEmails.containsKey(id)) {
			mapSensorEmails.put(id, getSensorEmailsFromService(id));
		}
		
	}
	private String[] getSensorEmailsFromService(long id) {
		String[] res = null;
		try {
			ResponseEntity<?> responseEntity = 
			restTemplate.exchange(getFullUrl(id), HttpMethod.GET, null, String[].class);
			if(!responseEntity.getStatusCode().is2xxSuccessful()) {
				throw new Exception((String) responseEntity.getBody());
			}
			res = (String[])responseEntity.getBody();
			mapSensorEmails.put(id, res);
		} catch (Exception e) {
			log.error("no sensor emails provided for sensor {}, reason: {}",
					id, e.getMessage());
			mapSensorEmails.remove(id);
			
		}
		log.debug("Range for sensor {} is {}", id, res);
		return res;
	}

	private String getFullUrl(long id) {
		String res = String.format("http://%s:%d%s/%d",
				providerConfiguration.getHost(),
				providerConfiguration.getPort(),
				providerConfiguration.getUrl(),
				id);
		log.debug("url:{}", res);
		return res;
	}

}