package telran.probes.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfiguration {
	
	@Value("#{${app.map.roles.uri}}")
	Map<String, String[]> authorizationMap;
	
	@Value("${app.security.pattern-method-delimeter}")
	String patternMethodDelimeter;
	@Value("${http.method.all}")
	String allHTTPMethods;
	
	@Bean
	SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http.cors(custom -> custom.disable());
		http.csrf(custom -> custom.disable());

		http.authorizeHttpRequests(requests -> {
			authorizationMap.forEach((patternMethod, roles) -> {
				String[] patternMethodArray = patternMethod.split(patternMethodDelimeter);
				if(patternMethodArray[1].equals(allHTTPMethods)) {
					requests.requestMatchers(patternMethodArray[0]).hasRole(roles);
				} else {
					requests.requestMatchers(HttpMethod.valueOf(patternMethodArray[1]), patternMethodArray[0]).hasRole(roles);
				}
			});
		});
		
		http.authorizeHttpRequests(requests -> requests.anyRequest().authenticated());
		http.httpBasic(Customizer.withDefaults());
		http.sessionManagement(custom -> custom.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));
		return http.build();
		
		
	
		.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/cars/get**").hasRole("READER")
				.requestMatchers("/cars/create**").hasRole("ADMIN")
				
				 //.requestMatchers("/cars/update/**").hasRole("ADMIN")  // it is set in controller
    			.requestMatchers("/hackerEntrance**").hasRole("ADMIN")
    			.anyRequest().permitAll() // for swagger
    			);
	
	}
	
	HttpSecurity setupAuthirization(var requests) {
		authorizationMap.forEach((key, value) -> {
			requests. .requestMatchers(HttpMethod.GET, "").hasRole("");
		};
		return requests;
	}
	
	
}
