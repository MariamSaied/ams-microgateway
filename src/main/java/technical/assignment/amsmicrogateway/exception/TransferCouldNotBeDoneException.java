package technical.assignment.amsmicrogateway.exception;

@SuppressWarnings("serial")
public class TransferCouldNotBeDoneException extends RuntimeException{
	public TransferCouldNotBeDoneException(String message) {
		super(message);
	}
}
