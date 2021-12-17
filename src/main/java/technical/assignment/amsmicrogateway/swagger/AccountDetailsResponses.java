package technical.assignment.amsmicrogateway.swagger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(value = RetentionPolicy.RUNTIME)
@ApiResponses(value = {
		
		@ApiResponse(responseCode = "200", description = "Success", 
				content = @Content(schema = @Schema(implementation = AccountDetailsResponseTemplate.class),
					examples = {
							@ExampleObject(name = "Success" ,value = "{\"code\":2000,\"message\":\"Success\",\"reason\":\"Success\",\"data\":{\"accountNumber\":\"23890487\",\"ownerFullName\":\"User1\",\"availableBalance\":5499.934}}"),
	
				}), 
				headers = {
					@Header(name = "Content-Type", description = "Retrieved content type", schema = @Schema(type = "string"))
					}),
		
		@ApiResponse(responseCode = "422",
		content = @Content(schema = @Schema(implementation = AccountDetailsResponseTemplate.class),
			examples = {
					@ExampleObject(name = "Invalid account number format" ,value = "{\"code\":4022,\"message\":\"Invalid account number format\",\"reason\":\"Unprocessable Entity\"}"),
					@ExampleObject(name = "Account not found" ,value = "{\"code\":6002,\"message\":\"No account found with the specified accountNumber\",\"reason\":\"Unprocessable Entity\"}")

		}), 
		headers = {
			@Header(name = "Content-Type", description = "Retrieved content type", schema = @Schema(type = "string"))
			})
})

public @interface AccountDetailsResponses {

	public String successCode() default "";

}