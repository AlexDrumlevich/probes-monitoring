package telran.probes.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;


public record SensorDto(@NotBlank long id, double minValue, double maxValue, int signalTimeRate, List<String> emails) {

}
