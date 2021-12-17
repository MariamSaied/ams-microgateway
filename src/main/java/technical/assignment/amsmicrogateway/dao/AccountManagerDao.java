package technical.assignment.amsmicrogateway.dao;

import java.util.Optional;

import technical.assignment.amsmicrogateway.dao.model.Account;
import technical.assignment.amsmicrogateway.rest.vm.MoneyTransferRequest;

public interface AccountManagerDao {

	public void loadAccounts();

	public Optional<Account> getAccountDetails(String accountNumber);
	
	public void transferMoney(MoneyTransferRequest moneyTransferRequest);

}
