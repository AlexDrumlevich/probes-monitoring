package telran.probe;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

import telran.probes.SensorImitation;

@SpringBootTest(classes = SensorImitation.class)
@Import(TestChannelBinderConfiguration.class)
public class SensorImitationTest {

	@Autowired
	ApplicationContext context;
	
	@Test
	void sensorImitationTest() throws InterruptedException {
		Thread.sleep(20000);
	}
}
