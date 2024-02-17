package telran.security.accounting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.exceptions.NotFoundException;
import telran.probes.dto.AccountDto;
import telran.security.accounting.model.Account;
import telran.security.accounting.repo.AccountRepo;


@Service
@Slf4j
@RequiredArgsConstructor
public class AccountingServiceImpl implements AccountingService{
	
final AccountRepo accountRepo;
final PasswordEncoder passwordEncoder;

	@Override
	public AccountDto addAccount(AccountDto accountDto) {
		String email = accountDto.email();
		AccountDto accountEncoded = getAccountDtoEncoded(accountDto);
		if(accountRepo.existsById(email)) {
			throw new IllegalStateException(String.format("account %s already exists", email));
		}
		Account account = accountRepo.save(Account.of(accountEncoded));
		log.debug("account {} has been saved", email);
		return account.build();
	}

	private AccountDto getAccountDtoEncoded(AccountDto accountDto) {
		
		return new AccountDto(accountDto.email(),
				passwordEncoder.encode(accountDto.password()), accountDto.roles());
	}

	@Override
	public AccountDto removeAccount(String email) {
		Account account = accountRepo.findById(email)
				.orElseThrow(()->new NotFoundException(String.format("Account %s doesn't exist", email)));
		accountRepo.delete(account);
		log.debug("account {} has been removed", email);
		return account.build();
	}
	
}
