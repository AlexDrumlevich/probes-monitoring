package telran.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import telran.probes.dto.ProbeData;

@Document(collection="probe_values")
@AllArgsConstructor
public class ProbeDataDoc {
	@Id
	long sensorId;
	float value;
	long timestamp;
	
	public static ProbeDataDoc of(ProbeData probeData) {
		return new ProbeDataDoc(probeData.sensorId(), probeData.value(), probeData.timestamp());
	}
	
	public ProbeData toDto() {
		return new ProbeData(sensorId, value, timestamp);
	}
}
