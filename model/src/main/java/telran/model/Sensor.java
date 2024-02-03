package telran.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import telran.probes.dto.SensorDto;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Document
@Builder
public class Sensor {

	@Id
	Long id;
	
	Double minValue;
	
	Double maxValue;
	
	Integer signalTimeRate;
	
	List<String> emails;
	
	
	public SensorDto toDto() {
		return new SensorDto(id, minValue, maxValue, signalTimeRate, emails);
	};
	
	static public Sensor of(SensorDto sensorDto) {
		return Sensor.builder()
				.id(sensorDto.id())
				.minValue(sensorDto.minValue())
				.maxValue(sensorDto.maxValue())
				.signalTimeRate(sensorDto.signalTimeRate())
				.emails(sensorDto.emails())
				.build();
	}
	
}
