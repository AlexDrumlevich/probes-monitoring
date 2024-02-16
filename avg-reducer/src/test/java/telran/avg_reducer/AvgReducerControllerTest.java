package telran.avg_reducer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import telran.probes.dto.ProbeDataDto;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
public class AvgReducerControllerTest {

	@Autowired
	InputDestination inputStrem;
	@Autowired
	OutputDestination outputStream;
	
	String bindingNameInput="avg-in-0";
	
	@Value("${app.binding_name_reduced_data_out}")
	String bindingNameOutput;
//	app.avg.binding.name=avg-values-out-0
	@MockBean
	AvgReducerService avgReducerService;
	
	ObjectMapper objectMapper = new ObjectMapper();
	
	ProbeDataDto inputProbeData = new ProbeDataDto(1, 10, 0);
	ProbeDataDto outputProbeData = new ProbeDataDto(1, 5, 0);
	
	@Test
	void avgReducerSendNothingTest() {
		when(avgReducerService.getAvgValue(inputProbeData)).thenReturn(null);
		inputStrem.send(new GenericMessage<ProbeDataDto>(inputProbeData), bindingNameInput);
		Message<byte[]> sentMessage = outputStream.receive(10, bindingNameOutput);
		assertNull(sentMessage);
	}
	
	@Test
	void avgReducerSendDataTest() throws Exception {
		when(avgReducerService.getAvgValue(inputProbeData)).thenReturn(5l);
		inputStrem.send(new GenericMessage<ProbeDataDto>(inputProbeData), bindingNameInput);
		Message<byte[]> sentMessage = outputStream.receive(10, bindingNameOutput);
		assertNotNull(sentMessage);
		ProbeDataDto sentProbeData = objectMapper.readValue(sentMessage.getPayload(), ProbeDataDto.class);
		assertEquals(sentProbeData, outputProbeData);
	}



	
}
