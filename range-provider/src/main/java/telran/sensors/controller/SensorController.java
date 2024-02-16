package telran.sensors.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.SensorRangeDto;
import telran.sensors.service.SensorRangeProviderService;

@RestController
//@RequestMapping("sensor")
@Slf4j
public class SensorController {

	@Autowired
	SensorRangeProviderService sensorRangeProviderService;
	
	//@GetMapping("${app.sensor.range.provider.url:/sensor/range} + /{id}")
	@GetMapping("/sensor/range/{id}")
	SensorRangeDto getSensorRange(@PathVariable long id) {
		SensorRangeDto sensorRange =  sensorRangeProviderService.findSensorRange(id);
		log.debug("sensor range received is {}", sensorRange);
		return sensorRange;
	}
	
}
