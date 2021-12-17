package technical.assignment.amsmicrogateway.exception;


@SuppressWarnings("serial")
public class AccountCouldNotBeFetchedException extends RuntimeException{
	public AccountCouldNotBeFetchedException(String message) {
		super(message);
	}
}
