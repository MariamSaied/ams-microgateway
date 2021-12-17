package technical.assignment.amsmicrogateway.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import technical.assignment.amsmicrogateway.dao.AccountManagerDao;
import technical.assignment.amsmicrogateway.dao.model.Account;
import technical.assignment.amsmicrogateway.exception.AccountCouldNotBeFetchedException;
import technical.assignment.amsmicrogateway.exception.AccountNotFoundException;
import technical.assignment.amsmicrogateway.exception.DestinationAccountNotFound;
import technical.assignment.amsmicrogateway.exception.NoAvailableBalanceException;
import technical.assignment.amsmicrogateway.exception.TransferCouldNotBeDoneException;
import technical.assignment.amsmicrogateway.rest.vm.MoneyTransferRequest;
import technical.assignment.amsmicrogateway.service.AccountManagementService;

@Service
@Log4j2
public class AccountManagementServiceImpl implements AccountManagementService {

	@Autowired
	private AccountManagerDao accountManagerDao;

	@Override
	public Account getAccountDetails(String accountNumber) {

		try {
			Optional<Account> accountOptional = accountManagerDao.getAccountDetails(accountNumber);

			if (!accountOptional.isPresent())
				throw new AccountNotFoundException();

			return accountOptional.get();

		} catch (AccountNotFoundException e) {
			throw e;
		} catch (Exception e) {
			throw new AccountCouldNotBeFetchedException(e.getLocalizedMessage());
		}

	}

	@Override
	public void transferMoney(MoneyTransferRequest moneyTransferRequest) {

		Account currentFromAccountState = getAccountDetails(moneyTransferRequest.getFromAccount());

		if (Boolean.FALSE.equals(doesAccountExist(moneyTransferRequest.getToAccount())))
			throw new DestinationAccountNotFound();

		if (currentFromAccountState.getAvailableBalance().compareTo(moneyTransferRequest.getAmount()) < 0)
			throw new NoAvailableBalanceException();

		try {

			accountManagerDao.transferMoney(moneyTransferRequest);

		} catch (Exception e) {
			throw new TransferCouldNotBeDoneException(e.getLocalizedMessage());
		}

	}

	Boolean doesAccountExist(String accountNumber) {

		boolean exists = true;

		try {
			getAccountDetails(accountNumber);
		} catch (AccountNotFoundException e) {
			log.info("doesAccountExist: account: {} doesn't exist", accountNumber);
			exists = false;
		}
		return exists;
	}

}
