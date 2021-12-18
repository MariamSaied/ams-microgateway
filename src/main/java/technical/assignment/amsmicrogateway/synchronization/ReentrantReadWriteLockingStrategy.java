package technical.assignment.amsmicrogateway.synchronization;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;
import technical.assignment.amsmicrogateway.dao.model.Account;
import technical.assignment.amsmicrogateway.rest.vm.MoneyTransferRequest;

/*this approach make sure that any read operation will wait if there's a write operation till it ends to ensure data consistency whenever we get Account details */
@Component
@Primary
@Log4j2
public class ReentrantReadWriteLockingStrategy implements ConcurrentTransactionSynchronizer {

	private ReadWriteLock lock = new ReentrantReadWriteLock();

	@Override
	public Optional<Account> getAccountDetails(String accountNumber,Map<String, Account> accounts) {

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
	public void transferMoney(MoneyTransferRequest moneyTransferRequest,Map<String, Account> accounts) {

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
