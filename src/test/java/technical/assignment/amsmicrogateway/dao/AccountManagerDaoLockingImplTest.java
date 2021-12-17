package technical.assignment.amsmicrogateway.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import technical.assignment.amsmicrogateway.dao.model.Account;
import technical.assignment.amsmicrogateway.rest.vm.MoneyTransferRequest;
import technical.assignment.amsmicrogateway.tests.util.ObjectFactory;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class AccountManagerDaoLockingImplTest {

	private static final String FROM_ACCOUNT = "23890487";
	private static final String TO_ACCOUNT = "23890488";

	@Autowired
	private AccountManagerDaoLockingImpl accountManagerLockingImpl;

	@Test
	void transferMoney_ConcurrentMoneyTransfer_ValidAvailableBalance() throws InterruptedException {

		ExecutorService service = Executors.newFixedThreadPool(3);
		CountDownLatch latch = new CountDownLatch(3);

		BigDecimal fromAccountBalanceBefore = accountManagerLockingImpl.getAccountDetails(FROM_ACCOUNT).get()
				.getAvailableBalance();

		BigDecimal toAccountBalanceBefore = accountManagerLockingImpl.getAccountDetails(TO_ACCOUNT).get()
				.getAvailableBalance();

		MoneyTransferRequest thread1Request = ObjectFactory.getMoneyTransferRequestInstance(FROM_ACCOUNT, TO_ACCOUNT,
				BigDecimal.valueOf(50));

		MoneyTransferRequest thread2Request = ObjectFactory.getMoneyTransferRequestInstance(TO_ACCOUNT, FROM_ACCOUNT,
				BigDecimal.valueOf(100));

		MoneyTransferRequest thread3Request = ObjectFactory.getMoneyTransferRequestInstance(FROM_ACCOUNT, TO_ACCOUNT,
				BigDecimal.valueOf(1000));

		service.execute(() -> {
			accountManagerLockingImpl.transferMoney(thread1Request);
			latch.countDown();
		});

		service.execute(() -> {
			accountManagerLockingImpl.transferMoney(thread2Request);
			latch.countDown();
		});

		service.execute(() -> {
			accountManagerLockingImpl.transferMoney(thread3Request);
			latch.countDown();
		});

		Account actualFromAccount = accountManagerLockingImpl.getAccountDetails(FROM_ACCOUNT).get();

		Account actualToAccount = accountManagerLockingImpl.getAccountDetails(TO_ACCOUNT).get();

		latch.await();

		assertEquals(fromAccountBalanceBefore.subtract(BigDecimal.valueOf(950)),
				actualFromAccount.getAvailableBalance());

		assertEquals(toAccountBalanceBefore.add(BigDecimal.valueOf(950)), actualToAccount.getAvailableBalance());
	}

	@Test
	void transferMoney_ConcurrentMoneyTransferWithGettingAccountDetails_ValidAvailableBalance()
			throws InterruptedException, ExecutionException {

		ExecutorService service = Executors.newFixedThreadPool(2);
		CountDownLatch latch = new CountDownLatch(2);

		BigDecimal fromAccountBalanceBefore = accountManagerLockingImpl.getAccountDetails(FROM_ACCOUNT).get()
				.getAvailableBalance();

		MoneyTransferRequest thread1Request = ObjectFactory.getMoneyTransferRequestInstance(FROM_ACCOUNT, TO_ACCOUNT,
				BigDecimal.valueOf(50));

		service.execute(() -> {
			accountManagerLockingImpl.transferMoney(thread1Request);
			latch.countDown();
		});

		Future<Optional<Account>> accountFuture = service.submit(() -> {
			Optional<Account> accountOptional = accountManagerLockingImpl.getAccountDetails(FROM_ACCOUNT);
			latch.countDown();
			return accountOptional;
		});

		latch.await();

		assertEquals(fromAccountBalanceBefore.subtract(BigDecimal.valueOf(50)),
				accountFuture.get().get().getAvailableBalance());

	}

}
