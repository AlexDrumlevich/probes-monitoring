package telran.probes.security.auth;

import java.util.Arrays;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.config.AccountProviderConfiguration;
import telran.probes.dto.AccountDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	final AccountProviderConfiguration accountProviderConfiguration;
	final RestTemplate restTemplate;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AccountDto accountDto = getAccountDto(username);
		String[] roles = Arrays.stream(accountDto.roles())
				.map(role -> "ROLE_" + role).toArray(String[]::new);
		log.debug("username: {}, password: {}, roles: {}",
				accountDto.email(), accountDto.password(), Arrays.deepToString(roles));
		return new User(accountDto.email(), accountDto.password(),
				AuthorityUtils.createAuthorityList(roles));
	}


	private AccountDto getAccountDto(String name) {
		AccountDto accountDto = null;

		try {
			ResponseEntity<?> responseEntity = restTemplate.getForEntity(getFullUrl(name), AccountDto.class);
			if(!responseEntity.getStatusCode().is2xxSuccessful()) {
				throw new Exception(responseEntity.getBody().toString());
			}
			accountDto = (AccountDto) responseEntity.getBody();
		} catch (Exception e) {
			log.error("no account for name {}, reason: {}",
					name, e.getMessage());
		}
		log.debug("AccountDto for name {} is {}", name, accountDto);
		return accountDto;

	}

	private String getFullUrl(String name) {
		String res = String.format("http://%s:%d%s/%s",
				accountProviderConfiguration.getHost(),
				accountProviderConfiguration.getPort(),
				accountProviderConfiguration.getUrl(),
				name);
		log.debug("url:{}", res);
		return res;
	}

}
