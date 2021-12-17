package technical.assignment.amsmicrogateway.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.ArgumentMatchers;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import technical.assignment.amsmicrogateway.dao.AccountManagerDao;
import technical.assignment.amsmicrogateway.dao.model.Account;
import technical.assignment.amsmicrogateway.exception.AccountCouldNotBeFetchedException;
import technical.assignment.amsmicrogateway.exception.AccountNotFoundException;
import technical.assignment.amsmicrogateway.exception.DestinationAccountNotFound;
import technical.assignment.amsmicrogateway.exception.NoAvailableBalanceException;
import technical.assignment.amsmicrogateway.exception.TransferCouldNotBeDoneException;
import technical.assignment.amsmicrogateway.rest.vm.MoneyTransferRequest;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class AccountManagementServiceImplTest {

	@MockBean
	private AccountManagerDao accountManagerDao;

	@SpyBean
	private AccountManagementServiceImpl accountManagementServiceImpl;

	@Test
	void getAccountDetails_existingAccount_AccountRetrievedCorrectly() {

		String accountNumber = "88888888";

		Optional<Account> mockedAccountOptional = Optional.of(new Account("88888888", "Test", BigDecimal.valueOf(500)));

		when(accountManagerDao.getAccountDetails(ArgumentMatchers.anyString())).thenReturn(mockedAccountOptional);

		Account actualAccountOptional = accountManagementServiceImpl.getAccountDetails(accountNumber);

		assertThat(actualAccountOptional).usingRecursiveComparison().isEqualTo(mockedAccountOptional.get());
	}

	@Test
	void getAccountDetails_NonExistingAccount_ThrowAccountNotFoundException() {

		String accountNumber = "88888888";

		when(accountManagerDao.getAccountDetails(ArgumentMatchers.anyString())).thenReturn(Optional.empty());

		assertThrows(AccountNotFoundException.class,
				() -> accountManagementServiceImpl.getAccountDetails(accountNumber));
	}

	@Test
	void getAccountDetails_UnhandledRuntimeException_ThrowAccountCouldNotBeFetchedException() {

		String accountNumber = "88888888";

		when(accountManagerDao.getAccountDetails(ArgumentMatchers.anyString())).thenThrow(NullPointerException.class);

		assertThrows(AccountCouldNotBeFetchedException.class,
				() -> accountManagementServiceImpl.getAccountDetails(accountNumber));
	}

	@Test
	void transferMoney_TransferSuccessfullyDone_NoExceptionThrown() {

		MoneyTransferRequest request = new MoneyTransferRequest("88888888", "88888889", BigDecimal.valueOf(1000));

		Account mockedFromAccount = new Account("88888888", "Test", BigDecimal.valueOf(4000));

		doReturn(mockedFromAccount).when(accountManagementServiceImpl).getAccountDetails(ArgumentMatchers.any());

		doReturn(true).when(accountManagementServiceImpl).doesAccountExist(ArgumentMatchers.any());
		
		doNothing().when(accountManagerDao).transferMoney(ArgumentMatchers.any());

		assertDoesNotThrow(() -> accountManagementServiceImpl.transferMoney(request));
	}

	@Test
	void transferMoney_NonExistingSourceAccount_ThrowAccountNotFoundException() {

		MoneyTransferRequest request = new MoneyTransferRequest("88888888", "88888889", BigDecimal.valueOf(1000));

		doThrow(AccountNotFoundException.class).when(accountManagementServiceImpl)
				.getAccountDetails(ArgumentMatchers.any());

		assertThrows(AccountNotFoundException.class, () -> accountManagementServiceImpl.transferMoney(request));
	}

	@Test
	void transferMoney_NonExistingDestinationAccount_ThrowDestinationAccountNotFound() {

		MoneyTransferRequest request = new MoneyTransferRequest("88888888", "88888889", BigDecimal.valueOf(1000));

		Account mockedFromAccount = new Account("88888888", "Test", BigDecimal.valueOf(500));

		doReturn(mockedFromAccount).when(accountManagementServiceImpl).getAccountDetails(ArgumentMatchers.any());

		doReturn(false).when(accountManagementServiceImpl).doesAccountExist(ArgumentMatchers.any());

		assertThrows(DestinationAccountNotFound.class, () -> accountManagementServiceImpl.transferMoney(request));
	}
	
	@Test
	void transferMoney_NoAvailableBalance_ThrowNoAvailableBalanceException() {

		MoneyTransferRequest request = new MoneyTransferRequest("88888888", "88888889", BigDecimal.valueOf(1000));

		Account mockedFromAccount = new Account("88888888", "Test", BigDecimal.valueOf(500));

		doReturn(mockedFromAccount).when(accountManagementServiceImpl).getAccountDetails(ArgumentMatchers.any());

		doReturn(true).when(accountManagementServiceImpl).doesAccountExist(ArgumentMatchers.any());

		assertThrows(NoAvailableBalanceException.class, () -> accountManagementServiceImpl.transferMoney(request));
	}
	
	@Test
	void transferMoney_UnhandledRuntimeException_ThrowTransferCouldNotBeDoneException() {

		MoneyTransferRequest request = new MoneyTransferRequest("88888888", "88888889", BigDecimal.valueOf(1000));

		Account mockedFromAccount = new Account("88888888", "Test", BigDecimal.valueOf(4000));

		doReturn(mockedFromAccount).when(accountManagementServiceImpl).getAccountDetails(ArgumentMatchers.any());

		doReturn(true).when(accountManagementServiceImpl).doesAccountExist(ArgumentMatchers.any());
		
		doThrow(NullPointerException.class).when(accountManagerDao).transferMoney(ArgumentMatchers.any());

		assertThrows(TransferCouldNotBeDoneException.class, () -> accountManagementServiceImpl.transferMoney(request));
	}

}
