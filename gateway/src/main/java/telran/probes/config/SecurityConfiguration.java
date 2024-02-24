package telran.probes.config;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;


@Configuration
@Slf4j
public class SecurityConfiguration {
	
	@Value("#{${app.map.roles.uri}}")
	Map<String, String[]> authorizationMap;
	
	@Value("${app.security.pattern-method-delimeter}")
	String patternMethodDelimeter;
	@Value("${http.method.all}")
	String allHTTPMethods;
	
	@Bean
	PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http.cors(custom -> custom.disable());
		http.csrf(custom -> custom.disable());
		
		http.authorizeHttpRequests(requests -> {
			authorizationMap.forEach((patternMethod, roles) -> {
				String[] patternMethodArray = patternMethod.split(patternMethodDelimeter);
				if(patternMethodArray[1].equals(allHTTPMethods)) {
					requests.requestMatchers(patternMethodArray[0]).hasAnyRole(roles);
					log.debug("Gateway security set requared roles: {} for pattern: {} for any HTTP methods", roles, patternMethodArray[0]);
				} else {
					requests.requestMatchers(HttpMethod.valueOf(patternMethodArray[1]), patternMethodArray[0]).hasAnyRole(roles);
					log.debug("Gateway security set requared roles: {} for pattern: {} for HTTP method: {}", roles, patternMethodArray[0], patternMethodArray[1]);
					
				}
			});
			requests.anyRequest().permitAll();
		});
		http.httpBasic(Customizer.withDefaults());
		
		//http.authorizeHttpRequests(requests -> requests.anyRequest().permitAll());
		
		//http.sessionManagement(custom -> custom.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));
		http.sessionManagement(custom -> custom.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		return http.build();
		
	}
	
	@PostConstruct
	void logMap() {
		authorizationMap.forEach((key, value) -> log.debug("urn-roles map is {}: {}", key, Arrays.deepToString(value)));
	}
	
}
