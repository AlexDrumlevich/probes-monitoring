
package telran.probes;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import telran.probes.dto.SensorRange;
import telran.probes.service.SensorRangeProviderConfiguration;
import telran.probes.service.SensorRangeProviderService;
@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class AnalyzerServiceTest {
	@Autowired
	InputDestination producer;
	String bindingProducerName = "ConfigChangeConsumer-in-0";

	@Autowired
	SensorRangeProviderConfiguration sensorRangeProviderConfiguration;
	@MockBean
	RestTemplate restTemplate;
	@Autowired
	SensorRangeProviderService providerService;
	@Autowired
	Consumer<String> configChangeConsumer;
	@Value("${app.update.token.range:range-update}")
	String rangeUpdateToken;
	@Value("${app.update.message.delimiter:#}")
	String delimiter;



	static final long SENSOR_ID = 123l;
	private static final float MIN_VALUE = 10;
	private static final float MAX_VALUE = 20;
	private static final float MIN_VALUE_UPDATED = -10;
	private static final float MAX_VALUE_UPDATED = 120;
	static final long SENSOR_ID2 = 124l;
	static final long SENSOR_ID_NOT_EXISTED = 126l;
	static final SensorRange SENSOR_RANGE = new SensorRange(MIN_VALUE, MAX_VALUE); 
	static final SensorRange SENSOR_RANGE_UPDATED = new SensorRange(MIN_VALUE_UPDATED, MAX_VALUE_UPDATED); 
	static SensorRange SENSOR_RANGE_DEFAULT; 


	class MyBool {
		static boolean value;
	}

	@BeforeEach
	void setUp() {
		MyBool.value = false;
		SENSOR_RANGE_DEFAULT = new SensorRange(sensorRangeProviderConfiguration.getMinDefaultValue(), sensorRangeProviderConfiguration.getMinDefaultValue());
	}

	@SuppressWarnings("unchecked")
	@Test
	@Order(1)
	void normalFlowWithNoMapData() {

		mockRestTemplateExchangeInvocation(new ResponseEntity<SensorRange>(SENSOR_RANGE, HttpStatus.OK));

		SensorRange actual = providerService.getSensorRange(SENSOR_ID);
		assertEquals(SENSOR_RANGE, actual);
		assertTrue(MyBool.value);

	}

	@Test
	@Order(2)
	void normalFlowWithMapData() {
		mockRestTemplateExchangeInvocation(null);

		SensorRange actual = providerService.getSensorRange(SENSOR_ID);
		assertEquals(SENSOR_RANGE, actual);
		assertFalse(MyBool.value);
	}

	@Test
	@Order(3)
	void normalFlowExistedRangeUpdate() {

		mockRestTemplateExchangeInvocation(new ResponseEntity<SensorRange>(SENSOR_RANGE_UPDATED, HttpStatus.OK));

		//checking before update
		SensorRange actual = providerService.getSensorRange(SENSOR_ID);
		assertEquals(SENSOR_RANGE, actual);

		//event for updating (invocation restTemplate.exchange())
		producer.send(
				new GenericMessage<String>(String.format("%s%s%s", rangeUpdateToken, delimiter, String.valueOf(SENSOR_ID))),
				bindingProducerName
				);

		//checking after updating
		SensorRange actualUpdated = providerService.getSensorRange(SENSOR_ID);
		assertEquals(SENSOR_RANGE_UPDATED, actualUpdated);
		assertTrue(MyBool.value);
	}

	@Test
	@Order(4)
	void normalFlowExistedRangeUpdateNotRangeValues() {
		//checking before update
		SensorRange actual = providerService.getSensorRange(SENSOR_ID);
		assertEquals(SENSOR_RANGE_UPDATED, actual);

		mockRestTemplateExchangeInvocation(new ResponseEntity<SensorRange>(SENSOR_RANGE_UPDATED, HttpStatus.OK));

		//event for updating (invocation restTemplate.exchange())
		producer.send(
				new GenericMessage<String>(String.format("%s%s%s", rangeUpdateToken, delimiter, String.valueOf(SENSOR_ID))),
				bindingProducerName
				);

		//checking after updating 
		SensorRange actualUpdated = providerService.getSensorRange(SENSOR_ID);
		assertEquals(SENSOR_RANGE_UPDATED, actualUpdated);
		assertTrue(MyBool.value);
	}

	@Test
	void normalFlowNotExistedRangeUpdate() {


		//checking before update
		SensorRange actual = providerService.getSensorRange(SENSOR_ID_NOT_EXISTED);
		assertEquals(SENSOR_RANGE_DEFAULT, actual);

		
		mockRestTemplateExchangeInvocation(new ResponseEntity<SensorRange>(SENSOR_RANGE_UPDATED, HttpStatus.OK));

		//event for updating (invocation restTemplate.exchange())
		producer.send(
				new GenericMessage<String>(String.format("%s%s%s", rangeUpdateToken, delimiter, String.valueOf(SENSOR_ID_NOT_EXISTED))),
				bindingProducerName
				);

		//checking after updating
		SensorRange actualUpdated = providerService.getSensorRange(SENSOR_ID_NOT_EXISTED);
		assertEquals(SENSOR_RANGE_UPDATED, actualUpdated);
		assertTrue(MyBool.value);
	}



	@Test
	void abnormalFlowWithResponceNotFound() {

		mockRestTemplateExchangeInvocation(new ResponseEntity<String>("", HttpStatus.NOT_FOUND));

		assertEquals(SENSOR_RANGE_DEFAULT, providerService.getSensorRange(SENSOR_ID2));
		assertTrue(MyBool.value);
	}

	@Test
	void abnormalFlowRestTemplateException() {

		when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(), any(Class.class)))
		.thenThrow(new RestClientException("RestClientException"));

		assertEquals(SENSOR_RANGE_DEFAULT, providerService.getSensorRange(SENSOR_ID2));

	}

	private <T> void mockRestTemplateExchangeInvocation(ResponseEntity<T> returnedValue) {
		when(restTemplate.exchange(anyString(), any(HttpMethod.class), any(),
				any(Class.class)))
		.thenAnswer(v -> {
			MyBool.value = true;
			return returnedValue;
		});
	}





}