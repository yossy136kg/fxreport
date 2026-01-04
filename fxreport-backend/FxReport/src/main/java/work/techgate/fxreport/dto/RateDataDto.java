package work.techgate.fxreport.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RateDataDto {
	String currency;
	BigDecimal rate;
	BigDecimal diff;
	BigDecimal diffPercent;
	BigDecimal avg7;
	List<BigDecimal> history;
	String aiSummary;
}