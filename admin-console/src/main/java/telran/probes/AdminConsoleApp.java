package telran.probes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("telran")
public class AdminConsoleApp {

	public static void main(String[] args) {
		SpringApplication.run(AdminConsoleApp.class, args);

	}

}
