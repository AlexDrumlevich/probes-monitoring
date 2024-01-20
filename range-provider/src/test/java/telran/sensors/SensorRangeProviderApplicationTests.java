package telran.sensors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.Module.SetupContext;

import telran.exceptions.NotFoundException;
import telran.sensors.controller.SensorController;
import telran.sensors.dto.SensorRangeDto;
import telran.model.Sensor;
import telran.sensors.repository.SensorRepository;
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
	
	List<Sensor> sensors = List.of(
			    new Sensor("1", 10.0, 100.0, null),
			    new Sensor("2", null, 100.0, null),
	            new Sensor("3", 10.0, null, null)
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
		
		assertEquals(expectedSensorRangeDto, sensorRangeProviderService.findSensor(expectedSensorRangeDto.id()));
	
		assertThrowsExactly(NotFoundException.class, () -> sensorRangeProviderService.findSensor("0"));
		
	}
	
	@Test
	void controllerTest() throws Exception {
		Sensor existedSensor = sensors.get(0);
		SensorRangeDto expectedSensorRangeDto = new SensorRangeDto(existedSensor.getId(), existedSensor.getMinValue(), existedSensor.getMaxValue());
		String expectedSensorRangeString = mapper.writeValueAsString(expectedSensorRangeDto);
		
		String response = mockMvc.perform(get("http://localhost:8080/sensor/1")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertEquals(expectedSensorRangeString, response);
//		
//		String response2 = mockMvc.perform(get("http://localhost:8080/sensor/null")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
//		assertEquals(expectedSensorRangeString, response2);	
	
		String badRequeExpected = mockMvc.perform(get("http://localhost:8080/sensor/0")).andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString(); 
		
		assertEquals(badRequeExpected, "Sensor id 0 not found");
		
		
	}

}
