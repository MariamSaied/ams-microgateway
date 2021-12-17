package technical.assignment.amsmicrogateway.dao.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Account {

	private String accountNumber;
	private String ownerFullName;
	private BigDecimal availableBalance;
	
}
