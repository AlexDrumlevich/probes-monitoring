package telran.probes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.SensorEmailsDto;
import telran.probes.dto.SensorRangeDto;
import telran.probes.service.EmailProviderService;

@RestController
//@RequestMapping("sensor")
@Slf4j

public class EmailProviderController {


		@Autowired
		EmailProviderService emailProviderService;
		
		//@GetMapping("${app.sensor.emails.provider.url:/sensor/emails} + /{id}")
		@GetMapping("/sensor/emails/{id}")
		SensorEmailsDto getSensorRange(@PathVariable long id) {
			SensorEmailsDto sensorRange =  emailProviderService.findSensorEmail(id);
			log.debug("sensor range received is {}", sensorRange);
			return sensorRange;
		}
	}