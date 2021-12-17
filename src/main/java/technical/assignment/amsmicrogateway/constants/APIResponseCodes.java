package technical.assignment.amsmicrogateway.constants;

public class APIResponseCodes {
	
	private APIResponseCodes() {
	}
	
	public static final Integer SUCCESS = 2000;
	public static final Integer UNPROCESSABLE_ENTITY = 4022;
	public static final Integer MICROGATEWAY_FAILURE = 5027;
	public static final Integer INVALID_INPUT_PARAMETER = 6001;
	public static final Integer ACCOUNT_NOT_FOUND = 6002;
	public static final Integer NO_AVAILABLE_BALANCE = 6003;
	public static final Integer DESTINATION_ACCOUNT_NOT_FOUND = 6004;
}
