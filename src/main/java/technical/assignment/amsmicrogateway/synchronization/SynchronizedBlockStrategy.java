package technical.assignment.amsmicrogateway.synchronization;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import technical.assignment.amsmicrogateway.dao.model.Account;
import technical.assignment.amsmicrogateway.rest.vm.MoneyTransferRequest;

/* this approach locking the writings on the same accounts and it doesn't apply any locks on the read operations*/
@Component
public class SynchronizedBlockStrategy implements ConcurrentTransactionSynchronizer {

	@Override
	public Optional<Account> getAccountDetails(String accountNumber,Map<String, Account> accounts) {

		Account account = accounts.get(accountNumber);
		return Optional.ofNullable(account);

	}

	@Override
	public void transferMoney(MoneyTransferRequest moneyTransferRequest,Map<String, Account> accounts) {

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
