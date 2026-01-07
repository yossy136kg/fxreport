package work.techgate.fxreport.job;

import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import work.techgate.fxreport.model.FxRateRaw;
import work.techgate.fxreport.service.FrankfurterApiService;
import work.techgate.fxreport.service.FxAiDailyReportService;
import work.techgate.fxreport.service.FxRateExpandService;
import work.techgate.fxreport.service.FxRateMetricsService;
import work.techgate.fxreport.service.FxUsdJpyAiCommentService;

@Slf4j
@Component
@RequiredArgsConstructor
public class FxDailyBatchJob {

	private final FrankfurterApiService frankfurterApiService;
	private final FxRateExpandService fxRateExpandService;
	private final FxRateMetricsService fxRateMetricsService;
	private final FxUsdJpyAiCommentService fxUsdJpyAiCommentService;
	private final FxAiDailyReportService fxAiDailyReportService;
	private final List<String> baseCurrencies = List.of("USD", "EUR", "GBP", "AUD", "CAD", "CHF", "NZD");

	/**
	 * 毎日 09:10 に実行（日本時間）
	 */
	@Scheduled(cron = "0 58 8 * * ?", zone = "Asia/Tokyo")
	public void executeDaily() {

		LocalDate targetDate = LocalDate.now();

		log.info("FX Daily Batch start: {}", targetDate);

		try {
			LocalDate rateDate = targetDate;
			for (String base : baseCurrencies) {
				FxRateRaw raw = frankfurterApiService.fetchByDate(base, targetDate);
				fxRateExpandService.expand(raw);
				rateDate = raw.getRateDate();
				fxRateMetricsService.calculateAll(base, rateDate);
			}

			// AI
			fxUsdJpyAiCommentService.generate(rateDate);
			fxAiDailyReportService.generate(rateDate);
			log.info("FX Daily Batch finished successfully: {}", targetDate);

		} catch (Exception e) {
			log.error("FX Daily Batch failed: {}", targetDate, e);
		}
	}
}
