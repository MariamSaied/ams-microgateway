package technical.assignment.amsmicrogateway.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import technical.assignment.amsmicrogateway.dao.model.Account;
import technical.assignment.amsmicrogateway.rest.vm.MoneyTransferRequest;

/* this approach locking the writings on the same accounts and it doesn't apply any locks on the read operations*/
@Service
public class AccountManagerDaoSynchronizedImpl implements AccountManagerDao {

	private Map<String, Account> accounts = new HashMap<>();

	@PostConstruct
	@Override
	public void loadAccounts() {

		accounts.put("23890487", Account.builder().accountNumber("23890487").ownerFullName("User1")
				.availableBalance(BigDecimal.valueOf(7000.60)).build());

		accounts.put("23890488", Account.builder().accountNumber("23890488").ownerFullName("User2")
				.availableBalance(BigDecimal.valueOf(5000.20)).build());

	}

	@Override
	public Optional<Account> getAccountDetails(String accountNumber) {

		Account account = accounts.get(accountNumber);
		return Optional.ofNullable(account);

	}

	@Override
	public void transferMoney(MoneyTransferRequest moneyTransferRequest) {

		Account fromAccount = accounts.get(moneyTransferRequest.getFromAccount());

		synchronized (fromAccount) {

			fromAccount
					.setAvailableBalance(fromAccount.getAvailableBalance().subtract(moneyTransferRequest.getAmount()));

			accounts.get(moneyTransferRequest.getToAccount())
					.setAvailableBalance(accounts.get(moneyTransferRequest.getToAccount()).getAvailableBalance()
							.add(moneyTransferRequest.getAmount()));

		}

	}

}
