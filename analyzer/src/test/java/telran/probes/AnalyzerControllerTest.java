package telran.probes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import com.fasterxml.jackson.databind.ObjectMapper;

import telran.probes.dto.ProbeDataDto;
import telran.probes.dto.ProbeDataDeviationDto;
import telran.probes.dto.SensorRangeDto;

import telran.probes.service.SensorRangeProviderService;
@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class AnalyzerControllerTest {
	private static final long SENSOR_ID = 123l;
	private static final double MIN_VALUE_NO_DEVIATION = 10;
	private static final double MAX_VALUE_NO_DEVIATION = 100;
	private static final double MIN_VALUE_DEVIATION = 60;
	private static final double MAX_VALUE_DEVIATION = 40;
	private static final SensorRangeDto SENSOR_RANGE_NO_DEVIATION =
			new SensorRangeDto(SENSOR_ID, MIN_VALUE_NO_DEVIATION, MAX_VALUE_NO_DEVIATION );
	private static final SensorRangeDto SENSOR_RANGE_MIN_DEVIATION =
			new SensorRangeDto(SENSOR_ID, MIN_VALUE_DEVIATION, MAX_VALUE_NO_DEVIATION );
	private static final SensorRangeDto SENSOR_RANGE_MAX_DEVIATION =
			new SensorRangeDto(SENSOR_ID, MIN_VALUE_DEVIATION, MAX_VALUE_DEVIATION );
	private static final float VALUE = 50f;
	static final ProbeDataDeviationDto DATA_MIN_DEVIATION = new ProbeDataDeviationDto(SENSOR_ID, VALUE, VALUE - MIN_VALUE_DEVIATION, 0);
	
	static final ProbeDataDto probeData = new ProbeDataDto(SENSOR_ID, VALUE, 0);
	ObjectMapper mapper = new ObjectMapper();
	@Autowired
InputDestination producer;
	@Autowired
	OutputDestination consumer;
	String bindingNameProducer="deviation-out-1";
	String bindingNameConsumer="consumerProbeData-in-0";
	@MockBean
	SensorRangeProviderService providerService;
	@MockBean(name = "ConfigChangeConsumer")
	Consumer<String> configChangeConsumer;
	
	@Test
	void noDeviationTest() {
		when(providerService.getSensorRange(SENSOR_ID))
		.thenReturn(SENSOR_RANGE_NO_DEVIATION);
		producer.send(new GenericMessage<ProbeDataDto>(probeData), bindingNameConsumer);
		Message<byte[]> message = consumer.receive(10, bindingNameProducer);
		assertNull(message);
	}
	@Test
	void minDeviationTest() throws Exception {
		when(providerService.getSensorRange(SENSOR_ID))
		.thenReturn(SENSOR_RANGE_MIN_DEVIATION);
		producer.send(new GenericMessage<ProbeDataDto>(probeData), bindingNameConsumer);
		Message<byte[]> message = consumer.receive(10, bindingNameProducer);
		assertNotNull(message);
		ProbeDataDeviationDto actualDeviation = mapper.readValue(message.getPayload(), ProbeDataDeviationDto.class);
		assertEquals(DATA_MIN_DEVIATION, actualDeviation);
		
	}

}
