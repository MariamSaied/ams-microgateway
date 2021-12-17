package technical.assignment.amsmicrogateway.tests.params;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import technical.assignment.amsmicrogateway.rest.vm.MoneyTransferRequest;

public class MoneyTransferInvalidInputParameters implements ArgumentsProvider {

	@Override
	public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
		return Stream.of(Arguments.of(new MoneyTransferRequest(" ", "88888888", BigDecimal.valueOf(90))),
				Arguments.of(new MoneyTransferRequest("88888888", "88888888", BigDecimal.valueOf(90))),
				Arguments.of(new MoneyTransferRequest("88888887", "88888888", BigDecimal.valueOf(-90))),
				Arguments.of(new MoneyTransferRequest("88888887", " ", BigDecimal.valueOf(90))));
	}
}
