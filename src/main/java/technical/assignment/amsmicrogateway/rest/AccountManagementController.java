package technical.assignment.amsmicrogateway.rest;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import technical.assignment.amsmicrogateway.constants.APIResponseCodes;
import technical.assignment.amsmicrogateway.constants.APIResponseMessages;
import technical.assignment.amsmicrogateway.dao.model.Account;
import technical.assignment.amsmicrogateway.rest.vm.APIGenericResponse;
import technical.assignment.amsmicrogateway.rest.vm.APIGenericResponseBuilder;
import technical.assignment.amsmicrogateway.rest.vm.MoneyTransferRequest;
import technical.assignment.amsmicrogateway.service.AccountManagementService;
import technical.assignment.amsmicrogateway.swagger.AccountDetailsResponseTemplate;
import technical.assignment.amsmicrogateway.swagger.AccountDetailsResponses;
import technical.assignment.amsmicrogateway.swagger.MoneyTransferResponses;
import technical.assignment.amsmicrogateway.swagger.SwaggerCommonsApiResponses;

@RestController
@Validated
@RequestMapping("${app.config.integration.api.base-uri}")
public class AccountManagementController {

	@Autowired
	private AccountManagementService accountManagerService;

	@GetMapping(value = "${app.config.integration.api.account-details-uri}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@SwaggerCommonsApiResponses
	@AccountDetailsResponses
	@Operation(summary = "Account Details", description = "Api to get account details")
	@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = AccountDetailsResponseTemplate.class)))
	public @ResponseBody APIGenericResponse<Account> getAccountDetails(
			@Valid @Pattern(regexp = "[\\d]{8}", message = APIResponseMessages.INVALID_ACCOUNT_NUMBER) @PathVariable("accountNumber") String accountNumber) {

		Account response = accountManagerService.getAccountDetails(accountNumber);

		return new APIGenericResponseBuilder<Account>().code(APIResponseCodes.SUCCESS)
				.message(APIResponseMessages.SUCCESS).reason(APIResponseMessages.SUCCESS).body(response).build();
	}

	@PatchMapping(value = "${app.config.integration.api.money-transfer-uri}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	@SwaggerCommonsApiResponses
	@MoneyTransferResponses
	@Operation(summary = "Transfer Money", description = "Api to transfer amount of money from account to another")
	@ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = APIGenericResponse.class)))
	public @ResponseBody APIGenericResponse<Object> transferMoney(
			@Validated @RequestBody MoneyTransferRequest moneyTransferRequest) {

		accountManagerService.transferMoney(moneyTransferRequest);

		return new APIGenericResponseBuilder<Object>().code(APIResponseCodes.SUCCESS)
				.message(APIResponseMessages.SUCCESS).reason(APIResponseMessages.SUCCESS).build();
	}

}
