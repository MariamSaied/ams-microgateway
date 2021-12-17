package technical.assignment.amsmicrogateway.rest.vm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(value = Include.NON_NULL)
public class APIGenericResponse<T> {

	@Schema(description = "Internal code mapping indicating success or error")
	private Integer code;
	@Schema(description = "Descriptive message indicating the exception cause")
	private String message;
	@Schema(description = "Generic reason of the cause of failure")
	private String reason;
	private T data;

}
