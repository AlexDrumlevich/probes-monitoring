package telran.probes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.SensorDto;
import telran.probes.dto.SensorEmailsDto;
import telran.probes.dto.SensorRangeDto;
import telran.probes.service.AdminConsoleService;


@RestController
@Slf4j
@RequestMapping("${app.sensor.admin-console.enter_point_urn}")
public class AdminConsoleController {

	@Autowired
	AdminConsoleService adminConsoleService;

	
	@GetMapping("${app.sensor.admin-console.get-sensors_urn}")
	List<SensorDto> getAllSensors() {
		return adminConsoleService.getAllSensors();
	}
	
	@PostMapping("${app.sensor.admin-console.update-range_urn}")
	SensorRangeDto updateSensorRange(@RequestBody @Valid SensorRangeDto sensorRangeDto) {
		log.debug("Controller in func updateSensorRange received: {}", sensorRangeDto);
		return adminConsoleService.updateSensorRange(sensorRangeDto);
	}

	@PostMapping("${app.sensor.admin-console.update-email_urn}")
	SensorEmailsDto updateSensorEmails(@RequestBody @Valid SensorEmailsDto sensorEmailsDto) {
		log.debug("Controller in func updateSensorEmails received: {}", sensorEmailsDto);
		return adminConsoleService.updateSensorEmails(sensorEmailsDto);
	}

	@PostMapping("${app.sensor.admin-console.add-sensor_urn}")
	SensorDto addSensor(@RequestBody @Valid SensorDto sensorDto) {
		log.debug("Controller in func addSensor received: {}", sensorDto);
		return adminConsoleService.addSensor(sensorDto);
	}


}
