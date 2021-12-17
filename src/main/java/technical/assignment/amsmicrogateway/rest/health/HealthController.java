package technical.assignment.amsmicrogateway.rest.health;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import technical.assignment.amsmicrogateway.rest.vm.HealthResponse;

@RestController
public class HealthController {

	@Operation(hidden = true)
	@Tag(name = "health", description = "This API to check the microservice health.")
	@GetMapping(value = { "${app.config.integration.api.health-base-url}" }, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<HealthResponse> imUpAndRunning() {

		return ResponseEntity.ok(new HealthResponse("UP"));
	}

}
