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
import telran.probes.dto.SensorRangeDto;
@Service
@RequiredArgsConstructor
@Slf4j
@Configuration
public class SensorRangeProviderServiceImpl implements SensorRangeProviderService {
	@Getter
	HashMap<Long, SensorRangeDto> mapRanges = new HashMap<>();
	@Value("${app.update.message.delimiter:#}")
	String delimiter;
	@Value("${app.update.token.range:range-update}")
	String rangeUpdateToken;
	final SensorRangeProviderConfiguration providerConfiguration;
	final RestTemplate restTemplate;
	@Override
	public SensorRangeDto getSensorRange(long sensorId) {
		SensorRangeDto range = mapRanges.get(sensorId);
		
		return range == null ? getRangeFromService(sensorId) : range;
	}
	
	@Bean(name = "ConfigChangeConsumer")
	Consumer<String> configChangeConsumer() {
		return this::checkConfigurationUpdate;
	}
	void checkConfigurationUpdate(String message) {
		
		String [] tokens = message.split(delimiter);
		if(tokens[0].equals(rangeUpdateToken)) {
			updateMapRanges(tokens[1]);
		}
	}
	private void updateMapRanges(String sensorIdStr) {
		long id = Long.parseLong(sensorIdStr);
		if (mapRanges.containsKey(id)) {
			mapRanges.put(id, getRangeFromService(id));
		}
		
	}
	private SensorRangeDto getRangeFromService(long id) {
		SensorRangeDto res =null;
		try {
			ResponseEntity<?> responseEntity = 
			restTemplate.exchange(getFullUrl(id), HttpMethod.GET, null, SensorRangeDto.class);
			if(!responseEntity.getStatusCode().is2xxSuccessful()) {
				throw new Exception((String) responseEntity.getBody());
			}
			res = (SensorRangeDto)responseEntity.getBody();
			mapRanges.put(id, res);
		} catch (Exception e) {
			log.error("no sensor range provided for sensor {}, reason: {}",
					id, e.getMessage());
			mapRanges.remove(id);
			res = new SensorRangeDto(id, providerConfiguration.getMinDefaultValue(),
					providerConfiguration.getMaxDefaultValue());
			log.warn("Taken default range {} - {}", res.minValue(), res.maxValue());
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