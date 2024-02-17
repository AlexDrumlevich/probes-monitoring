package telran.security.accounting.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import telran.probes.dto.AccountDto;
import telran.security.accounting.service.AccountingService;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountingController {
	final AccountingService accountingService;
	@PostMapping
	AccountDto addAccount(@RequestBody @Valid AccountDto accountDto) {
		return accountingService.addAccount(accountDto);
	}
	@DeleteMapping("{email}")
	AccountDto removeAccount(@PathVariable String email) {
		return accountingService.removeAccount(email);
	}
}