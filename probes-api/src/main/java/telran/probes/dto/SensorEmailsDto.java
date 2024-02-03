package telran.probes.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;

public record SensorEmailsDto(@NotNull Long id, @NotNull List<String> emails) {

}
