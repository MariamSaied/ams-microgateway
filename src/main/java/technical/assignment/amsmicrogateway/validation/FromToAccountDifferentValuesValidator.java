package technical.assignment.amsmicrogateway.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import technical.assignment.amsmicrogateway.rest.vm.MoneyTransferRequest;

public class FromToAccountDifferentValuesValidator
		implements ConstraintValidator<ValidFromToAccountDifferentValues, MoneyTransferRequest> {

	@Override
	public boolean isValid(MoneyTransferRequest moneyTransferRquest,
			ConstraintValidatorContext constraintValidatorContext) {
	
		return (!moneyTransferRquest.getFromAccount().equals(moneyTransferRquest.getToAccount()));
	}

}