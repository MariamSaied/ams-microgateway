package technical.assignment.amsmicrogateway.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import technical.assignment.amsmicrogateway.dao.model.Account;
import technical.assignment.amsmicrogateway.rest.vm.MoneyTransferRequest;

/*this approach make sure that any read operation will wait if there's a write operation till it ends to ensure data consistency whenever we get Account details */
@Service
@Primary
@Log4j2
public class AccountManagerDaoLockingImpl implements AccountManagerDao {

	private Map<String, Account> accounts = new HashMap<>();
	private ReadWriteLock lock = new ReentrantReadWriteLock();

	@PostConstruct
	@Override
	public void loadAccounts() {

		accounts.put("23890487", Account.builder().accountNumber("23890487").ownerFullName("User1")
				.availableBalance(BigDecimal.valueOf(7000.60)).build());

		accounts.put("23890488", Account.builder().accountNumber("23890488").ownerFullName("User2")
				.availableBalance(BigDecimal.valueOf(5000.20)).build());

		log.info("Accounts created successfully!");
	}

	@Override
	public Optional<Account> getAccountDetails(String accountNumber) {

		Optional<Account> accountOptional = Optional.empty();
		lock.readLock().lock();

		try {
			log.info("Current thread id:{}, reading account: {}", Thread.currentThread().getId(), accountNumber);
			Account account = accounts.get(accountNumber);
			accountOptional = Optional.ofNullable(account);
		} finally {
			lock.readLock().unlock();
		}

		return accountOptional;
	}

	@Override
	public void transferMoney(MoneyTransferRequest moneyTransferRequest) {

		lock.writeLock().lock();

		try {
			log.info("Current thread id applying transfer: {}, fromAccount: {}, toAccount: {}, amount: {}",
					Thread.currentThread().getId(), moneyTransferRequest.getFromAccount(),
					moneyTransferRequest.getToAccount(), moneyTransferRequest.getAmount());

			accounts.get(moneyTransferRequest.getFromAccount())
					.setAvailableBalance(accounts.get(moneyTransferRequest.getFromAccount()).getAvailableBalance()
							.subtract(moneyTransferRequest.getAmount()));

			accounts.get(moneyTransferRequest.getToAccount())
					.setAvailableBalance(accounts.get(moneyTransferRequest.getToAccount()).getAvailableBalance()
							.add(moneyTransferRequest.getAmount()));
		} finally {
			lock.writeLock().unlock();
		}
	}
}
