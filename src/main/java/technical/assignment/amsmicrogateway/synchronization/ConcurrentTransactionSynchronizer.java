package technical.assignment.amsmicrogateway.synchronization;

import java.util.Map;
import java.util.Optional;

import technical.assignment.amsmicrogateway.dao.model.Account;
import technical.assignment.amsmicrogateway.rest.vm.MoneyTransferRequest;

public interface ConcurrentTransactionSynchronizer {

	public Optional<Account> getAccountDetails(String accountNumber,Map<String, Account> accounts);

	public void transferMoney(MoneyTransferRequest moneyTransferRequest,Map<String, Account> accounts);
}
