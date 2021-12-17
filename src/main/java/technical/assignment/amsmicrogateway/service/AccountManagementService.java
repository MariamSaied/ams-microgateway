package technical.assignment.amsmicrogateway.service;

import technical.assignment.amsmicrogateway.dao.model.Account;
import technical.assignment.amsmicrogateway.rest.vm.MoneyTransferRequest;

public interface AccountManagementService {

	public Account getAccountDetails(String accountNumber);

	public void transferMoney(MoneyTransferRequest moneyTransferRequest);
}
