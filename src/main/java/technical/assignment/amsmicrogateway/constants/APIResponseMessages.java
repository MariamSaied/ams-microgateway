package technical.assignment.amsmicrogateway.constants;

public class APIResponseMessages {
	
	private APIResponseMessages() {
	}
	
	public static final String SUCCESS = "Success";
	public static final String NEGATIVE_AMOUNT = "Property amount must be greater than 0";
	public static final String ACCOUNT_NOT_FOUND = "No account found with the specified accountNumber";
	public static final String NO_AVAILABLE_BALANCE = "No enough available balance for specified fromAccount";
	public static final String DESTINATION_ACCOUNT_NOT_FOUND = "Destination account not found";
	public static final String INVALID_ACCOUNT_NUMBER = "Invalid account number format";
	public static final String FROM_ACCOUNT_CANNOT_BE_SAME_AS_TO_ACCOUNT = "Property fromAccoun cannot be same as toAccount";
	public static final String MANDATORY_ACCOUNT_NUMBER = "Missing mandatory account number";
	public static final String MANDATORY_AMOUNT_FIELD = "Missing mandatory amount field";

}
