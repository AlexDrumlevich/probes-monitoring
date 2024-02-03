package telran.probes.dto;

import java.util.Objects;

public record ProbeDataDeviationDto(long sensorId, double value, double deviation, long timestamp) {

	@Override
	public int hashCode() {
		return Objects.hash(deviation, sensorId, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProbeDataDeviationDto other = (ProbeDataDeviationDto) obj;
		return Double.doubleToLongBits(deviation) == Double.doubleToLongBits(other.deviation) && sensorId == other.sensorId
				&& Double.doubleToLongBits(value) == Double.doubleToLongBits(other.value);
	}

}
