package telran.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Document
public class Sensor {

	@Id
	String id;
	
	Double minValue;
	
	Double maxValue;
	
	List<String> emails;
	
}
