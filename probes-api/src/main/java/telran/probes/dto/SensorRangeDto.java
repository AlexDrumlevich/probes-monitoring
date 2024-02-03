package telran.probes.dto;


import jakarta.validation.constraints.NotNull;

public record SensorRangeDto(@NotNull(message = "Id must not be null") Long id, @NotNull(message = "Min range value must not be null") Double minValue, @NotNull(message = "Max range value must not be null") Double maxValue) {

}
