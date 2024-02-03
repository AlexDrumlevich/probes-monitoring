package telran.sensors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import telran.exceptions.NotFoundException;
import telran.sensors.controller.SensorController;

import telran.model.Sensor;
import telran.probes.dto.SensorRangeDto;
import telran.sensors.service.SensorRangeProviderService;

@AutoConfigureWebMvc
@AutoConfigureMockMvc
@SpringBootTest
class SensorRangeProviderApplicationTests {

	@Autowired
	SensorRangeProviderService sensorRangeProviderService;
	@Autowired
	MongoTemplate mongoTemplate;
	@Autowired
	SensorController controller;
	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper mapper;
	@Value("${sensor.collection.name}")
	String collectioName;
	

	@Value("${app.sensor.range.provider.url:/sensor/range}")
	String url;
	
	List<Sensor> sensors = List.of(
			    new Sensor(1l, 10.0, 100.0, 1000, List.of("email1@gmail.com")),
			    new Sensor(2l, 0.0, 100.0, 2000, List.of("email2@gmail.com")),
	            new Sensor(3l, 10.0, 20.0, 3000, List.of("email3@gmail.com"))
			);


	@BeforeEach
	void setup() {
		mongoTemplate.dropCollection(collectioName);
		mongoTemplate.createCollection(collectioName);
		sensors.stream().forEach(s -> mongoTemplate.save(s, collectioName));
	}
	
	@Test
	void serviceTest() {
		Sensor existedSensor = sensors.get(0);
		SensorRangeDto expectedSensorRangeDto = new SensorRangeDto(existedSensor.getId(), existedSensor.getMinValue(), existedSensor.getMaxValue());
		
		assertEquals(expectedSensorRangeDto, sensorRangeProviderService.findSensorRange(expectedSensorRangeDto.id()));
	
		assertThrowsExactly(NotFoundException.class, () -> sensorRangeProviderService.findSensorRange
				(0));
		
	}
	
	@Test
	void controllerTest() throws Exception {
		Sensor existedSensor = sensors.get(0);
		SensorRangeDto expectedSensorRangeDto = new SensorRangeDto(existedSensor.getId(), existedSensor.getMinValue(), existedSensor.getMaxValue());
		String expectedSensorRangeString = mapper.writeValueAsString(expectedSensorRangeDto);
		
		String response = mockMvc.perform(get("http://localhost:8080" + url + "/1")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertEquals(expectedSensorRangeString, response);

		
	
		String badRequeExpected = mockMvc.perform(get("http://localhost:8080" + url + "/0")).andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString(); 
		
		assertEquals(badRequeExpected, "Sensor id 0 not found");
		
		
	}

}
