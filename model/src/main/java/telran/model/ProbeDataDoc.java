package telran.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import telran.probes.dto.ProbeDataDto;

@Document(collection="probe_values")

public class ProbeDataDoc {
	public ProbeDataDoc(long sensorId, float value, long timestamp) {
		this.sensorId = sensorId;
		this.value = value;
		this.timestamp = timestamp;
	}

	@Id
	long sensorId;
	float value;
	long timestamp;
	
	public static ProbeDataDoc of(ProbeDataDto probeData) {
		return new ProbeDataDoc(probeData.sensorId(), probeData.value(), probeData.timestamp());
	}
	
	public ProbeDataDto toDto() {
		return new ProbeDataDto(sensorId, value, timestamp);
	}
}
