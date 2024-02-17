package telran.probes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {
	@Bean
	PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http.cors(custom -> custom.disable());
		http.csrf(custom -> custom.disable());

		http.authorizeHttpRequests(requests -> requests.anyRequest().authenticated());
		http.authorizeHttpRequests(requests -> requests.requestMatchers(HttpMethod.GET).permitAll());

		http.httpBasic(Customizer.withDefaults());
		return http.build();
	}
}
