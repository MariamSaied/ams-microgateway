package technical.assignment.amsmicrogateway.rest.vm;

import io.swagger.v3.oas.annotations.media.Schema;

public class HealthResponse {
	@Schema(description = "The Status")
	private String status;

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @param status
	 */
	public HealthResponse(String status) {
		super();
		this.status = status;
	}
	
	
	
}
