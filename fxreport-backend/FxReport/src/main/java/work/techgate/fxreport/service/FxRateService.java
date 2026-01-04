package work.techgate.fxreport.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import work.techgate.fxreport.model.FxRate;
import work.techgate.fxreport.repository.FxRateRepository;

@Service
@RequiredArgsConstructor
public class FxRateService {

	private final FxRateRepository fxRateRepository;

	public List<BigDecimal> getLast7DaysRates(String baseCcy, String quoteCcy, LocalDate today) {
		LocalDate startDate = today.minusDays(6); // 過去6日分 + 当日 = 計7日
		List<FxRate> rates = fxRateRepository.findByBaseCcyAndQuoteCcyAndRateDateBetweenOrderByRateDateAsc(baseCcy,
				quoteCcy, startDate, today);

		return rates.stream().map(FxRate::getRate).toList();
	}
}
