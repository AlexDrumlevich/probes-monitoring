package telran.avg_populator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.support.GenericMessage;

import telran.avg_populator.repo.AvgPopulatorRepo;
import telran.probes.dto.ProbeData;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
public class AvgPopulatorTest {

	
	@Autowired
	InputDestination imputStream;
	
	@Autowired
	AvgPopulatorRepo avgPopulatorRepo;
	
	String bindingNameInput="avgPopulating-in-0";
	
	long timestamp = System.currentTimeMillis();
	ProbeData probeDataExpexted = new ProbeData(1l, 12.3f, timestamp);
	
	@BeforeEach
	void setup() {
		avgPopulatorRepo.deleteAll();
	}
	
	@Test
	void commonTest() {
		imputStream.send(new GenericMessage<ProbeData>(probeDataExpexted), bindingNameInput);
		ProbeData probeDataActual = avgPopulatorRepo.findById(1l).get().toDto();
		 assertEquals(probeDataExpexted, probeDataActual);
	}
	
}
