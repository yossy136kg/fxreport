package work.techgate.fxreport.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import work.techgate.fxreport.model.FxRateRaw;

@Service
@RequiredArgsConstructor
public class FxBatchService {

	private final FrankfurterApiService frankfurterApiService;
	private final FxRateExpandService fxRateExpandService;
	private final FxRateMetricsService fxRateMetricsService;
	private final FxUsdJpyAiCommentService fxUsdJpyAiCommentService;
	private final FxAiDailyReportService fxAiDailyReportService;
	private final List<String> baseCurrencies = List.of("USD", "EUR", "GBP", "AUD", "CAD", "CHF", "NZD");

	public void run(LocalDate date) throws IOException {
		for (String base : baseCurrencies) {
			FxRateRaw raw = frankfurterApiService.fetchByDate(base, date);
			fxRateExpandService.expand(raw);
			fxRateMetricsService.calculateAll(base, date);
		}

		// AI
		fxUsdJpyAiCommentService.generate(date);
		fxAiDailyReportService.generate(date);
	}
}
