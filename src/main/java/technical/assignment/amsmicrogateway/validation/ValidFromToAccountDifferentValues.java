package technical.assignment.amsmicrogateway.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import technical.assignment.amsmicrogateway.constants.APIResponseMessages;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { FromToAccountDifferentValuesValidator.class })
public @interface ValidFromToAccountDifferentValues {

    String message() default APIResponseMessages.FROM_ACCOUNT_CANNOT_BE_SAME_AS_TO_ACCOUNT;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}