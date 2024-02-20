package telran.probes.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.probes.dto.AccountDto;
import telran.probes.service.AccountProviderService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AccountProviderController {
	final AccountProviderService accountingService;	
	@GetMapping("${app.sensor.account.provider.url}" + "/{email}")
	AccountDto getAccount(@PathVariable String email) {
		return accountingService.getAccount(email);
	}
}