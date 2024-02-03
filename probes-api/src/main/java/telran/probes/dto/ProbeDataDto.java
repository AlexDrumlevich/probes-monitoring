package telran.probes.dto;

import java.util.Objects;

public record ProbeDataDto(long sensorId, float value, long timestamp) {

	@Override
	public int hashCode() {
		return Objects.hash(sensorId, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProbeDataDto other = (ProbeDataDto) obj;
		return sensorId == other.sensorId && Float.floatToIntBits(value) == Float.floatToIntBits(other.value);
	}

}
