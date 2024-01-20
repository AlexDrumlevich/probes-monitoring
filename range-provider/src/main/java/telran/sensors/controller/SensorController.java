package telran.sensors.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.constraints.NotEmpty;
import telran.sensors.dto.SensorRangeDto;
import telran.sensors.service.SensorRangeProviderService;

@RestController
@RequestMapping("sensor")
public class SensorController {

	@Autowired
	SensorRangeProviderService sensorRangeProviderService;
	
	@GetMapping("/{id}")
	SensorRangeDto getSensorRange(@PathVariable @NotEmpty String id) {
		return sensorRangeProviderService.findSensor(id);
	}
	
}
