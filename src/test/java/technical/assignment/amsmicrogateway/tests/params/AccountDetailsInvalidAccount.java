package technical.assignment.amsmicrogateway.tests.params;

import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class AccountDetailsInvalidAccount implements ArgumentsProvider {

	@Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
		return Stream.of(Arguments.of(" "),
				Arguments.of("7876"),
				Arguments.of("7777777j"));
    }
}
