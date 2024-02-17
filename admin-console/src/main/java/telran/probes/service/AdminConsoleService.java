package telran.probes.service;


import java.util.List;

import telran.probes.dto.SensorDto;
import telran.probes.dto.SensorEmailsDto;
import telran.probes.dto.SensorRangeDto;

public interface AdminConsoleService {

	SensorRangeDto updateSensorRange(SensorRangeDto sensorRange);
	SensorEmailsDto updateSensorEmails(SensorEmailsDto sensorEmails);
	SensorDto addSensor(SensorDto sensor);
	List<SensorDto> getAllSensors();

	
}
