package technical.assignment.amsmicrogateway.rest.vm;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import technical.assignment.amsmicrogateway.constants.APIResponseMessages;
import technical.assignment.amsmicrogateway.validation.ValidFromToAccountDifferentValues;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ValidFromToAccountDifferentValues(message = APIResponseMessages.FROM_ACCOUNT_CANNOT_BE_SAME_AS_TO_ACCOUNT)
public class MoneyTransferRequest {
	
	@Pattern(regexp="[\\d]{8}",message = APIResponseMessages.INVALID_ACCOUNT_NUMBER)
	@NotEmpty(message = APIResponseMessages.MANDATORY_ACCOUNT_NUMBER)
	@Schema(required = true,description = "Account number to transfer money from",maxLength = 8,minLength = 8)
	private String fromAccount;
	@Pattern(regexp="[\\d]{8}",message = APIResponseMessages.INVALID_ACCOUNT_NUMBER)
	@NotEmpty(message = APIResponseMessages.MANDATORY_ACCOUNT_NUMBER)
	@Schema(required = true,description = "Account number to transfer money to",maxLength = 8,minLength = 8)
	private String toAccount;
	@Min(value = 0,message = APIResponseMessages.NEGATIVE_AMOUNT)
	@NotNull(message = APIResponseMessages.MANDATORY_AMOUNT_FIELD)
	@Schema(required = true,description = "Money amount to be transfered",allOf = BigDecimal.class )
	private BigDecimal amount;
	
}
