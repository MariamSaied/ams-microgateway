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
import technical.assignment.amsmicrogateway.rest.vm.APIGenericResponse;

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(value = RetentionPolicy.RUNTIME)
@ApiResponses(value = {
		
		@ApiResponse(responseCode = "200", description = "Success", 
				content = @Content(schema = @Schema(implementation = APIGenericResponse.class),
					examples = {
							@ExampleObject(name = "Success" ,value = "{\"code\":2000,\"message\":\"Success\",\"reason\":\"Success\"}"),
	
				}), 
				headers = {
					@Header(name = "Content-Type", description = "Retrieved content type", schema = @Schema(type = "string"))
					}),
		
		@ApiResponse(responseCode = "422", 
		content = @Content(schema = @Schema(implementation = APIGenericResponse.class),
			examples = {
					@ExampleObject(name = "Source account not found" ,value = "{\"code\":6002,\"message\":\"No account found with the specified accountNumber\",\"reason\":\"Unprocessable Entity\"}"),
					@ExampleObject(name = "Destination account not found" ,value = "{\"code\":6004,\"message\":\"{\"code\":6004,\"message\":\"Destination account not found\",\"reason\":\"Unprocessable Entity\"}"),
					@ExampleObject(name = "Invalid transfer amount" ,value = "{\"code\":6001,\"message\":\"Property amount must be greater than 0\",\"reason\":\"Unprocessable Entity\"}"),
					@ExampleObject(name = "Source and destination accounts are same" ,value = "{\"code\":6001,\"message\":\"Property fromAccoun cannot be same as toAccount\",\"reason\":\"Unprocessable Entity\"}"),
					@ExampleObject(name = "Invalid account number format" ,value = "{\"code\":6001,\"message\":\"Invalid account number format\",\"reason\":\"Unprocessable Entity\"}"),
					@ExampleObject(name = "No enough available balance" ,value = "{\"code\":6003,\"message\":\"No enough available balance for specified fromAccount\",\"reason\":\"Unprocessable Entity\"}"),
					@ExampleObject(name = "Missing mandatory amount" ,value = "{\"code\":6001,\"message\":\"Missing mandatory amount field\",\"reason\":\"Unprocessable Entity\"}"),
					@ExampleObject(name = "Missing toAccount/fromAccount number" ,value = "{\"code\":6001,\"message\":\"Missing mandatory account number\",\"reason\":\"Unprocessable Entity\"}")

		}), 
		headers = {
			@Header(name = "Content-Type", description = "Retrieved content type", schema = @Schema(type = "string"))
			})
})	

public @interface MoneyTransferResponses {

	public String successCode() default "";

}