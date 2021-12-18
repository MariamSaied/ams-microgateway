package technical.assignment.amsmicrogateway.synchronization;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.BeforeAll;
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
class SynchronizedBlockStrategyTest {

	private static final String FROM_ACCOUNT = "23890487";
	private static final String TO_ACCOUNT = "23890488";
	private Map<String, Account> accounts = new HashMap<>();
	
	@Autowired
	private SynchronizedBlockStrategy synchronizedBlockStrategy;

	@BeforeAll
	private void loadAccounts() {
		accounts.put("23890487", Account.builder().accountNumber("23890487").ownerFullName("User1")
				.availableBalance(BigDecimal.valueOf(7000.60)).build());

		accounts.put("23890488", Account.builder().accountNumber("23890488").ownerFullName("User2")
				.availableBalance(BigDecimal.valueOf(5000.20)).build());
	}
	
	@Test
	void transferMoney_ConcurrentMoneyTransfer_ValidAvailableBalance() throws InterruptedException {

		ExecutorService service = Executors.newFixedThreadPool(3);
		CountDownLatch latch = new CountDownLatch(3);

		BigDecimal fromAccountBalanceBefore = synchronizedBlockStrategy.getAccountDetails(FROM_ACCOUNT,accounts).get()
				.getAvailableBalance();

		BigDecimal toAccountBalanceBefore = synchronizedBlockStrategy.getAccountDetails(TO_ACCOUNT,accounts).get()
				.getAvailableBalance();

		MoneyTransferRequest thread1Request = ObjectFactory.getMoneyTransferRequestInstance(FROM_ACCOUNT, TO_ACCOUNT,
				BigDecimal.valueOf(50));

		MoneyTransferRequest thread2Request = ObjectFactory.getMoneyTransferRequestInstance(TO_ACCOUNT, FROM_ACCOUNT,
				BigDecimal.valueOf(100));

		MoneyTransferRequest thread3Request = ObjectFactory.getMoneyTransferRequestInstance(FROM_ACCOUNT, TO_ACCOUNT,
				BigDecimal.valueOf(1000));

		service.execute(() -> {
			synchronizedBlockStrategy.transferMoney(thread1Request,accounts);
			latch.countDown();
		});

		service.execute(() -> {
			synchronizedBlockStrategy.transferMoney(thread2Request,accounts);
			latch.countDown();
		});

		service.execute(() -> {
			synchronizedBlockStrategy.transferMoney(thread3Request,accounts);
			latch.countDown();
		});

		Account actualFromAccount = synchronizedBlockStrategy.getAccountDetails(FROM_ACCOUNT,accounts).get();

		Account actualToAccount = synchronizedBlockStrategy.getAccountDetails(TO_ACCOUNT,accounts).get();

		latch.await();

		assertEquals(fromAccountBalanceBefore.subtract(BigDecimal.valueOf(950)),
				actualFromAccount.getAvailableBalance());

		assertEquals(toAccountBalanceBefore.add(BigDecimal.valueOf(950)), actualToAccount.getAvailableBalance());
	}

}
