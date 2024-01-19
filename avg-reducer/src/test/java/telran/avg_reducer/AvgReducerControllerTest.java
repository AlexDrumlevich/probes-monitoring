package telran.avg_reducer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import com.fasterxml.jackson.databind.ObjectMapper;

import telran.avg_reducer.service.AvgReducerService;
import telran.probes.dto.ProbeData;



@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
public class AvgReducerControllerTest {

	@Autowired
	InputDestination inputStrem;
	@Autowired
	OutputDestination outputStream;
	
	String bindingNameInput="avg-in-0";
	String bindingNameOutput="avg-out-1";
	
	@MockBean
	AvgReducerService avgReducerService;
	
	ObjectMapper objectMapper = new ObjectMapper();
	
	ProbeData inputProbeData = new ProbeData(1, 10, 0);
	ProbeData outputProbeData = new ProbeData(1, 5, 0);
	
	@Test
	void avgReducerSendNothingTest() {
		when(avgReducerService.getAvgValue(inputProbeData)).thenReturn(null);
		inputStrem.send(new GenericMessage<ProbeData>(inputProbeData), bindingNameInput);
		Message<byte[]> sentMessage = outputStream.receive(10, bindingNameOutput);
		assertNull(sentMessage);
	}
	
	@Test
	void avgReducerSendDataTest() throws Exception {
		when(avgReducerService.getAvgValue(inputProbeData)).thenReturn(5l);
		inputStrem.send(new GenericMessage<ProbeData>(inputProbeData), bindingNameInput);
		Message<byte[]> sentMessage = outputStream.receive(10, bindingNameOutput);
		assertNotNull(sentMessage);
		ProbeData sentProbeData = objectMapper.readValue(sentMessage.getPayload(), ProbeData.class);
		assertEquals(sentProbeData, outputProbeData);
	}


}
