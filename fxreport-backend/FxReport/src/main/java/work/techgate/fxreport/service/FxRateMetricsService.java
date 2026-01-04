package work.techgate.fxreport.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import work.techgate.fxreport.model.FxRate;
import work.techgate.fxreport.model.FxRateMetrics;
import work.techgate.fxreport.repository.FxRateMetricsRepository;
import work.techgate.fxreport.repository.FxRateRepository;

@Service
@RequiredArgsConstructor
public class FxRateMetricsService {

	private final FxRateRepository fxRateRepository;
	private final FxRateMetricsRepository fxRateMetricsRepository;

	@Transactional
	public void calculateAll(String baseCcy, LocalDate date) {

		List<FxRate> rates = fxRateRepository.findByBaseCcyAndRateDate(baseCcy, date);

		for (FxRate rate : rates) {
			calculate(baseCcy, rate.getQuoteCcy(), date);
		}
	}

	/**
	 * 指定日・指定通貨の metrics を計算
	 */
	@Transactional
	private void calculate(String baseCcy, String quoteCcy, LocalDate targetDate) {

		FxRate today = fxRateRepository.findByBaseCcyAndQuoteCcyAndRateDate(baseCcy, quoteCcy, targetDate)
				.orElseThrow(() -> new IllegalStateException("rate not found"));

		// 直近7日分（当日含む、昇順）
		List<FxRate> history = fxRateRepository.findByBaseCcyAndQuoteCcyOrderByRateDateAsc(baseCcy, quoteCcy).stream()
				.filter(r -> !r.getRateDate().isAfter(targetDate)).toList();

		BigDecimal closeRate = today.getRate();

		// 前日差・前日比
		BigDecimal diffPrev = null;
		BigDecimal pctPrev = null;

		if (history.size() >= 2) {
			FxRate prev = history.get(history.size() - 2);
			diffPrev = closeRate.subtract(prev.getRate());
			pctPrev = diffPrev.divide(prev.getRate(), 6, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
		}

		// 7日移動平均
		BigDecimal ma7 = history.stream().skip(Math.max(0, history.size() - 7)).map(FxRate::getRate)
				.reduce(BigDecimal.ZERO, BigDecimal::add)
				.divide(BigDecimal.valueOf(Math.min(7, history.size())), 6, RoundingMode.HALF_UP);

		// トレンド判定（単純版）
		String trend7d = closeRate.compareTo(ma7) > 0 ? "UP" : closeRate.compareTo(ma7) < 0 ? "DOWN" : "FLAT";

		FxRateMetrics metrics = fxRateMetricsRepository
				.findByBaseCcyAndQuoteCcyAndRateDate(baseCcy, quoteCcy, targetDate)
				.orElse(FxRateMetrics.builder().baseCcy(baseCcy).quoteCcy(quoteCcy).rateDate(targetDate).build());

		metrics.setCloseRate(closeRate);
		metrics.setDiffPrev(diffPrev);
		metrics.setPctPrev(pctPrev);
		metrics.setMa7(ma7);
		metrics.setTrend7d(trend7d);
		metrics.setCreatedAt(OffsetDateTime.now());

		fxRateMetricsRepository.save(metrics);
	}
}
