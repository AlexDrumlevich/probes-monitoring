package telran.avg_reducer;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import telran.avg_reducer.repo.ProbesListRepo;
import telran.avg_reducer.service.AvgReducerService;
import telran.model.ProbesList;
import telran.probes.dto.ProbeDataDto;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.*;

@SpringBootTest
public class AvgReducerServiceTest {
	static List<Float> VALUES_NO_AVG;
	static List<Float> VALUES_AVG;
	static final long SENSOR_ID_NO_REDIS_RECORD = 123l;
	static final long SENSOR_ID_NO_AVG = 124l;
	static final long SENSOR_ID_AVG = 125l;
	static final float VALUE = 100f;
	static final ProbeDataDto PROBE_NO_REDIS_RECORD = new ProbeDataDto(SENSOR_ID_NO_REDIS_RECORD, VALUE, 0);
	static final ProbeDataDto PROBE_NO_AVG = new ProbeDataDto(SENSOR_ID_NO_AVG, VALUE, 0);
	static final ProbeDataDto PROBE_AVG = new ProbeDataDto(SENSOR_ID_AVG, VALUE, 0);
	static final ProbesList PROBES_LIST_NO_AVG = new ProbesList(SENSOR_ID_NO_AVG);
	static final ProbesList PROBES_LIST_AVG = new ProbesList(SENSOR_ID_AVG);
	static final ProbesList PROBES_LIST_NO_RECORD = new ProbesList(SENSOR_ID_NO_REDIS_RECORD);
	static final Map<Long, ProbesList> mapRedis = new HashMap<>();
	@Autowired
	AvgReducerService avgReduceService;	
	@MockBean
	ProbesListRepo probesListRepo;
	@Value("${app.average.reducing.size}")
	int reducingSize;
	
	@BeforeEach
	void setUp() {
		VALUES_NO_AVG = PROBES_LIST_NO_AVG.getValues();
		VALUES_AVG = PROBES_LIST_AVG.getValues();
		VALUES_AVG.clear();
		VALUES_AVG.add(VALUE);
		mapRedis.put(SENSOR_ID_NO_AVG, PROBES_LIST_NO_AVG);
		mapRedis.put(SENSOR_ID_AVG, PROBES_LIST_AVG);
	}
	
	@Test
	void testNoRedisRecord() {
		when(probesListRepo.findById(SENSOR_ID_NO_REDIS_RECORD)).thenReturn(Optional.ofNullable(null));
		when(probesListRepo.save(PROBES_LIST_NO_RECORD)).thenAnswer(new Answer<ProbesList>() {

			@Override
			public ProbesList answer(InvocationOnMock invocation) throws Throwable {
				mapRedis.put(SENSOR_ID_NO_REDIS_RECORD, invocation.getArgument(0));
				return invocation.getArgument(0);
			}
		});
		Long res = avgReduceService.getAvgValue(PROBE_NO_REDIS_RECORD);
		assertNull(res);
		ProbesList probesList = mapRedis.get(SENSOR_ID_NO_REDIS_RECORD);
		assertNotNull(probesList);
		assertEquals(VALUE, probesList.getValues().get(0));
	}

	
	@Test
	void testNoAvgValue() {
		//TODO
		when(probesListRepo.findById(SENSOR_ID_NO_AVG)).thenReturn(Optional.of(PROBES_LIST_NO_AVG));
		when(probesListRepo.save(PROBES_LIST_NO_AVG)).thenAnswer(new Answer<ProbesList>() {

			@Override
			public ProbesList answer(InvocationOnMock invocation) throws Throwable {
				ProbesList invocationProbeList = invocation.getArgument(0, ProbesList.class);
				mapRedis.put(invocationProbeList.getSensorId(), invocationProbeList);
				return null;
			}
			
		});
		int valuesSize = mapRedis.get(SENSOR_ID_NO_AVG).getValues().size();
		Long avg = avgReduceService.getAvgValue(PROBE_NO_AVG);
		assertNull(avg);
		ProbesList probesList = mapRedis.get(PROBES_LIST_NO_AVG.getSensorId());
		assertEquals(valuesSize + 1, probesList.getValues().size());
		assertEquals(VALUE, probesList.getValues().get(0));
	
		
		
	}
	@Test
	void testAvgValue() {
		//TODO
		when(probesListRepo.findById(SENSOR_ID_AVG)).thenReturn(Optional.of(PROBES_LIST_AVG));
		when(probesListRepo.save(PROBES_LIST_AVG)).thenAnswer(new Answer<ProbesList>() {

			@Override
			public ProbesList answer(InvocationOnMock invocation) throws Throwable {
				ProbesList invocationProbeList = invocation.getArgument(0, ProbesList.class);
				mapRedis.put(invocationProbeList.getSensorId(), invocationProbeList);
				return null;
			}
		});
		
		int valuesSize = mapRedis.get(SENSOR_ID_AVG).getValues().size();
		Long avg = avgReduceService.getAvgValue(PROBE_AVG);
		assertEquals(avg, (long) VALUE);
		ProbesList probesList = mapRedis.get(PROBES_LIST_AVG.getSensorId());
		assertTrue(probesList.getValues().isEmpty());
		assertEquals(valuesSize, reducingSize - 1);
				
		
	}
	
	
}