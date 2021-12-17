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
		
		@ApiResponse(responseCode = "500", description = "Internal Server Error", 
				content = @Content(schema = @Schema(implementation = APIGenericResponse.class),
					examples = {
							@ExampleObject(name = "Microgateway failure" ,value = "{\"code\":5027,\"message\":\"exception\",\"reason\":\"INTERNAL SERVER ERROR\"}"),
	
				}), 
				headers = {
					@Header(name = "Content-Type", description = "Retrieved content type", schema = @Schema(type = "string"))
					})
})

public @interface SwaggerCommonsApiResponses {

	public String successCode() default "";

}