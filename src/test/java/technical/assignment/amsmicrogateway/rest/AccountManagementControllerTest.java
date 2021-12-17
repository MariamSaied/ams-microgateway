package technical.assignment.amsmicrogateway.rest;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import technical.assignment.amsmicrogateway.constants.APIResponseCodes;
import technical.assignment.amsmicrogateway.dao.model.Account;
import technical.assignment.amsmicrogateway.exception.AccountCouldNotBeFetchedException;
import technical.assignment.amsmicrogateway.exception.AccountNotFoundException;
import technical.assignment.amsmicrogateway.exception.DestinationAccountNotFound;
import technical.assignment.amsmicrogateway.exception.NoAvailableBalanceException;
import technical.assignment.amsmicrogateway.exception.TransferCouldNotBeDoneException;
import technical.assignment.amsmicrogateway.rest.vm.MoneyTransferRequest;
import technical.assignment.amsmicrogateway.service.AccountManagementService;
import technical.assignment.amsmicrogateway.tests.params.AccountDetailsInvalidAccount;
import technical.assignment.amsmicrogateway.tests.params.MoneyTransferInvalidInputParameters;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
class AccountManagementControllerTest {

	@Value("${app.config.integration.api.base-uri}")
	private String amsBaseUrI;

	@Value("${app.config.integration.api.account-details-uri}")
	private String getAccountDetailsUri;

	@Value("${app.config.integration.api.money-transfer-uri}")
	private String transferMoneyUri;

	@MockBean
	private AccountManagementService accountManagementService;

	@InjectMocks
	private AccountManagementController accountManagementController;

	@Autowired
	private MockMvc mvc;

	@Test
	void getAccountDetails_SuccessfulRequest_SuccessResponse() throws Exception {

		String accountNumber = "88888888";

		Account response = new Account(accountNumber, "Test", BigDecimal.valueOf(1000));

		when(accountManagementService.getAccountDetails(ArgumentMatchers.any())).thenReturn(response);

		this.mvc.perform(get(amsBaseUrI + getAccountDetailsUri, accountNumber).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(2000))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("$.data.accountNumber").value(response.getAccountNumber()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data.ownerFullName").value(response.getOwnerFullName()))
				.andExpect(
						MockMvcResultMatchers.jsonPath("$.data.availableBalance").value(response.getAvailableBalance()))
				.andDo(print());

	}

	@ParameterizedTest
	@ArgumentsSource(AccountDetailsInvalidAccount.class)
	void getAccountDetails_InvalidAccountNumber_UnprocessableEntity(String invalidAccountNumber) throws Exception {

		this.mvc.perform(
				get(amsBaseUrI + getAccountDetailsUri, invalidAccountNumber).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
				.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(APIResponseCodes.UNPROCESSABLE_ENTITY))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist()).andDo(print());

	}

	@Test
	void transferMoney_AccountNotFoundException_UnprocessableEntityWithAccountNotFoundCode()
			throws Exception {
		
		String accountNumber = "88888888";
				
		when(accountManagementService.getAccountDetails(ArgumentMatchers.any())).thenThrow(AccountNotFoundException.class);
		
		this.mvc.perform(get(amsBaseUrI + getAccountDetailsUri,accountNumber).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
				.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(APIResponseCodes.ACCOUNT_NOT_FOUND))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist()).andDo(print());

	}
	@Test
	void getAccountDetails_AccountCouldNotBeFetchedException_InternalServerErrorWithMicrogatewayFailureCode()
			throws Exception {

		String accountNumber = "88888888";

		when(accountManagementService.getAccountDetails(ArgumentMatchers.any()))
				.thenThrow(AccountCouldNotBeFetchedException.class);

		this.mvc.perform(get(amsBaseUrI + getAccountDetailsUri, accountNumber).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(APIResponseCodes.MICROGATEWAY_FAILURE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist()).andDo(print());

	}
	
	@Test
	void transferMoney_SuccessfulRequest_SuccessResponse()
			throws Exception {
		
		MoneyTransferRequest request = new MoneyTransferRequest("88888887", "88888888", BigDecimal.valueOf(90));
		
		doNothing().when(accountManagementService).transferMoney(ArgumentMatchers.any());
		
		this.mvc.perform(patch(amsBaseUrI + transferMoneyUri).accept(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(
						request))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(APIResponseCodes.SUCCESS))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist()).andDo(print());

	}
	
	@ParameterizedTest
	@ArgumentsSource(MoneyTransferInvalidInputParameters.class)
	void transferMoney_InvalidInputParameters_UnprocessableEntityWithInvalidInputParameterCode(MoneyTransferRequest request)
			throws Exception {
		
		this.mvc.perform(patch(amsBaseUrI + transferMoneyUri).accept(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(
						request))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
				.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(APIResponseCodes.INVALID_INPUT_PARAMETER))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist()).andDo(print());

	}
	
	@Test
	void transferMoney_SourceAccountNotFoundException_UnprocessableEntityWithAccountNotFoundCode()
			throws Exception {
		
		MoneyTransferRequest request = new MoneyTransferRequest("88888887", "88888888", BigDecimal.valueOf(90));
				
		doThrow(AccountNotFoundException.class).when(accountManagementService).transferMoney(ArgumentMatchers.any());
		
		this.mvc.perform(patch(amsBaseUrI + transferMoneyUri).accept(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(
						request))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
				.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(APIResponseCodes.ACCOUNT_NOT_FOUND))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist()).andDo(print());

	}

	@Test
	void transferMoney_DestinationAccountNotFoundException_UnprocessableEntityWithDestinationAccountNotFoundCode()
			throws Exception {
		
		MoneyTransferRequest request = new MoneyTransferRequest("88888887", "88888888", BigDecimal.valueOf(90));
				
		doThrow(DestinationAccountNotFound.class).when(accountManagementService).transferMoney(ArgumentMatchers.any());
		
		this.mvc.perform(patch(amsBaseUrI + transferMoneyUri).accept(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(
						request))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
				.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(APIResponseCodes.DESTINATION_ACCOUNT_NOT_FOUND))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist()).andDo(print());

	}
	
	@Test
	void transferMoney_NoAvailableBalanceException_UnprocessableEntityWithNoAvailableBalanceExceptionCode()
			throws Exception {
		
		MoneyTransferRequest request = new MoneyTransferRequest("88888887", "88888888", BigDecimal.valueOf(90));
				
		doThrow(NoAvailableBalanceException.class).when(accountManagementService).transferMoney(ArgumentMatchers.any());
		
		this.mvc.perform(patch(amsBaseUrI + transferMoneyUri).accept(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(
						request))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
				.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(APIResponseCodes.NO_AVAILABLE_BALANCE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist()).andDo(print());

	}
	
	@Test
	void transferMoney_TransferCouldNotBeDoneException_InternalServerErrorWithMicrogatewayFailureCode()
			throws Exception {
		
		MoneyTransferRequest request = new MoneyTransferRequest("88888887", "88888888", BigDecimal.valueOf(90));
				
		doThrow(TransferCouldNotBeDoneException.class).when(accountManagementService).transferMoney(ArgumentMatchers.any());
		
		this.mvc.perform(patch(amsBaseUrI + transferMoneyUri).accept(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(
						request))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(APIResponseCodes.MICROGATEWAY_FAILURE))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data").doesNotExist()).andDo(print());

	}
}
