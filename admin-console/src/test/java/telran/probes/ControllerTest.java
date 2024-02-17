package telran.probes;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.constraints.AssertTrue;
import telran.exceptions.NotFoundException;
import telran.probes.dto.SensorEmailsDto;
import telran.probes.dto.SensorRangeDto;
import telran.probes.service.AdminConsoleService;
import telran.probes.service.IllegalRangeException;

@WebMvcTest
@Disabled
public class ControllerTest {

	@MockBean
	AdminConsoleService service;
	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper mapper;
	
	SensorRangeDto sensorRangeDtoNormal = new SensorRangeDto(1l, 10.0, 100.0);
	SensorRangeDto sensorRangeDtoMissingFields = new SensorRangeDto(null, 10.0, 100.0);
	String wrongFieldTypeSensorRangeDto = "{\"id\":a2,\"minValue\":1.0,\"maxValue\":33.0}";

	
	
	SensorEmailsDto expectedSensorEmailsDto = new SensorEmailsDto(2l, List.of("email1@gmail.com", "email1@gmail.com"));
	
	
@Test
@DisplayName("Controller test: sensor range update normal flow")
	void sensorRangeUpdateNormalFlow() throws Exception {
		when(service.updateSensorRange(sensorRangeDtoNormal)).thenReturn(sensorRangeDtoNormal);
		String rangeJSON = mapper.writeValueAsString(sensorRangeDtoNormal);
		System.out.println(rangeJSON);
		String response = mockMvc
				.perform(
						post("/sensor/range")
						.contentType(MediaType.APPLICATION_JSON)
						.content(rangeJSON)
						)
				.andExpect(
						status()
						.isOk()
						)
				.andReturn().getResponse().getContentAsString();
		assertEquals(rangeJSON, response);
		
	}

@Test
@DisplayName("Controller test: sensor range update missing fields")
	void sensorRangeUpdateMissingFields() throws Exception {
		when(service.updateSensorRange(sensorRangeDtoMissingFields)).thenReturn(sensorRangeDtoMissingFields);
		String rangeJSON = mapper.writeValueAsString(sensorRangeDtoMissingFields);
	
		String response = mockMvc
				.perform(
						post("/sensor/range")
						.contentType(MediaType.APPLICATION_JSON)
						.content(rangeJSON)
						)
				.andExpect(
						status()
						.isBadRequest()
						)
				.andReturn().getResponse().getContentAsString();
		assertEquals("Id must not be null", response.strip());
		
	}

@Test
@DisplayName("Controller test: sensor range update wrong field type")
	void sensorRangeUpdateWrongFieldType() throws Exception {
	
		String response = mockMvc
				.perform(
						post("/sensor/range")
						.contentType(MediaType.APPLICATION_JSON)
						.content(wrongFieldTypeSensorRangeDto)
						)
				.andExpect(
						status()
						.isBadRequest()
						)
				.andReturn().getResponse().getContentAsString();
	
		assertTrue(response.contains("parse error"));
		
	}


@Test
@DisplayName("Controller test: sensor range update gets NotFoundException from service")
	void sensorRangeUpdateGetsNotFoundExceptionFromService() throws Exception {
	when(service.updateSensorRange(sensorRangeDtoNormal)).thenThrow(new NotFoundException("not found"));
	String rangeJSON = mapper.writeValueAsString(sensorRangeDtoNormal);
		String response = mockMvc
				.perform(
						post("/sensor/range")
						.contentType(MediaType.APPLICATION_JSON)
						.content(rangeJSON)
						)
				.andExpect(
						status()
						.isBadRequest()
						)
				.andReturn().getResponse().getContentAsString();
	
		assertEquals("not found", response.strip());
		
	}

@Test
@DisplayName("Controller test: sensor range update gets IllegalRangeException from service")
	void sensorRangeUpdateGetsIllegalRangeExceptionFromService() throws Exception {
	when(service.updateSensorRange(sensorRangeDtoNormal)).thenThrow(new IllegalRangeException("IllegalRangeException"));
	String rangeJSON = mapper.writeValueAsString(sensorRangeDtoNormal);
		String response = mockMvc
				.perform(
						post("/sensor/range")
						.contentType(MediaType.APPLICATION_JSON)
						.content(rangeJSON)
						)
				.andExpect(
						status()
						.isBadRequest()
						)
				.andReturn().getResponse().getContentAsString();
	
		assertEquals("IllegalRangeException", response.strip());
		
	}
}
