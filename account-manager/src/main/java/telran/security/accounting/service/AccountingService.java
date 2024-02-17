package telran.security.accounting.service;

import telran.probes.dto.AccountDto;

public interface AccountingService {
	AccountDto addAccount(AccountDto accountDto);
	AccountDto removeAccount(String email);
}
