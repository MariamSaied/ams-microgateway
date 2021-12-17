package technical.assignment.amsmicrogateway.rest.vm;

public class APIGenericResponseBuilder<T> {

	private APIGenericResponse<T> apiResponse;

	public APIGenericResponseBuilder() {

		apiResponse = new APIGenericResponse<>();
	}

	public APIGenericResponseBuilder<T> body(T data) {

		this.apiResponse.setData(data);

		return this;
	}

	public APIGenericResponseBuilder<T> code(int code) {

		this.apiResponse.setCode(code);

		return this;
	}

	public APIGenericResponseBuilder<T> reason(String reason) {

		this.apiResponse.setReason(reason);

		return this;
	}

	public APIGenericResponseBuilder<T> message(String message) {

		this.apiResponse.setMessage(message);

		return this;
	}

	public APIGenericResponse<T> build() {

		return this.apiResponse;
	}

}
