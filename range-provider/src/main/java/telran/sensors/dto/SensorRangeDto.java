package telran.sensors.dto;

import jakarta.validation.constraints.NotNull;

public record SensorRangeDto(@NotNull String id, Double minValue, Double maxValue) {

}
