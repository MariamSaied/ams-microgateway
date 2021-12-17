package technical.assignment.amsmicrogateway.exception;

import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import technical.assignment.amsmicrogateway.constants.APIResponseCodes;
import technical.assignment.amsmicrogateway.constants.APIResponseMessages;
import technical.assignment.amsmicrogateway.rest.vm.APIGenericResponse;
import technical.assignment.amsmicrogateway.rest.vm.APIGenericResponseBuilder;

@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	HttpHeaders customHeaders = new HttpHeaders();

	@PostConstruct
	private void init() {
		customHeaders.setContentType(MediaType.APPLICATION_JSON);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<APIGenericResponse<Object>> handleViolationException(ConstraintViolationException ex) {

		StringBuilder message = new StringBuilder();
		Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
		for (ConstraintViolation<?> violation : violations) {
			message.append(violation.getMessage());
		}

		return new ResponseEntity<APIGenericResponse<Object>>(
				new APIGenericResponseBuilder<Object>().code(Integer.valueOf(APIResponseCodes.UNPROCESSABLE_ENTITY))
						.message(message.toString()).reason(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase()).build(),
				customHeaders, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler({ AccountCouldNotBeFetchedException.class, TransferCouldNotBeDoneException.class })
	public final ResponseEntity<Object> handleAccountCouldNotBeFetchedException(Exception ex) {

		return new ResponseEntity<>(new APIGenericResponseBuilder<>()
				.code(Integer.valueOf(APIResponseCodes.MICROGATEWAY_FAILURE)).message(ex.getLocalizedMessage())
				.reason(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).build(), customHeaders,
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(AccountNotFoundException.class)
	public final ResponseEntity<Object> handleAccountNotFoundException(AccountNotFoundException ex) {

		return new ResponseEntity<>(
				new APIGenericResponseBuilder<>().code(Integer.valueOf(APIResponseCodes.ACCOUNT_NOT_FOUND))
						.message(APIResponseMessages.ACCOUNT_NOT_FOUND)
						.reason(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase()).build(),
				customHeaders, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(NoAvailableBalanceException.class)
	public final ResponseEntity<Object> handleNoAvailableBalanceException(NoAvailableBalanceException ex) {

		return new ResponseEntity<>(
				new APIGenericResponseBuilder<>().code(Integer.valueOf(APIResponseCodes.NO_AVAILABLE_BALANCE))
						.message(APIResponseMessages.NO_AVAILABLE_BALANCE)
						.reason(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase()).build(),
				customHeaders, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(DestinationAccountNotFound.class)
	public final ResponseEntity<Object> handleDestinationAccountNotFound(DestinationAccountNotFound ex) {

		return new ResponseEntity<>(
				new APIGenericResponseBuilder<>().code(Integer.valueOf(APIResponseCodes.DESTINATION_ACCOUNT_NOT_FOUND))
						.message(APIResponseMessages.DESTINATION_ACCOUNT_NOT_FOUND)
						.reason(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase()).build(),
				customHeaders, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();

		return new ResponseEntity<>(
				new APIGenericResponseBuilder<>().code(Integer.valueOf(APIResponseCodes.INVALID_INPUT_PARAMETER))
						.message(!fieldErrors.isEmpty() ? fieldErrors.get(0).getDefaultMessage()
								: globalErrors.get(0).getDefaultMessage())
						.reason(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase()).build(),
				customHeaders, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleGeneralException(Exception ex, WebRequest request) {

		return new ResponseEntity<>(new APIGenericResponseBuilder<Object>()
				.code(Integer.valueOf(APIResponseCodes.MICROGATEWAY_FAILURE)).message(ex.getLocalizedMessage())
				.reason(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).build(), customHeaders,
				HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
