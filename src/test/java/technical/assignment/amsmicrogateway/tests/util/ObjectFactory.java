package technical.assignment.amsmicrogateway.tests.util;

import java.math.BigDecimal;

import technical.assignment.amsmicrogateway.rest.vm.MoneyTransferRequest;

public class ObjectFactory {

	public static MoneyTransferRequest getMoneyTransferRequestInstance(String fromAccount,String toAccount,BigDecimal amount) {
		return new MoneyTransferRequest(fromAccount,toAccount,amount);
	}
}
