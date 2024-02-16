package telran.probes.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;


public record SensorDto(long id, double minValue, double maxValue, int signalTimeRate, List<String> emails) {

}
