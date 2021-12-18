package technical.assignment.amsmicrogateway.dao.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import technical.assignment.amsmicrogateway.dao.AccountManagerDao;
import technical.assignment.amsmicrogateway.dao.model.Account;
import technical.assignment.amsmicrogateway.rest.vm.MoneyTransferRequest;
import technical.assignment.amsmicrogateway.synchronization.ConcurrentTransactionSynchronizer;

@Service
public class AccountManagerImpl implements AccountManagerDao {

	private Map<String, Account> accounts = new HashMap<>();

	@Autowired
	private ConcurrentTransactionSynchronizer synchronizer;

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

		return synchronizer.getAccountDetails(accountNumber, accounts);

	}

	@Override
	public void transferMoney(MoneyTransferRequest moneyTransferRequest) {

		synchronizer.transferMoney(moneyTransferRequest, accounts);

	}

}
