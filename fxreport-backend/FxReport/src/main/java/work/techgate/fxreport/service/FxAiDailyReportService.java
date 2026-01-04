package work.techgate.fxreport.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import work.techgate.fxreport.ai.AiClient;
import work.techgate.fxreport.model.FxAiDailyReport;
import work.techgate.fxreport.model.FxRateMetrics;
import work.techgate.fxreport.repository.FxAiDailyReportRepository;
import work.techgate.fxreport.repository.FxRateMetricsRepository;

@Service
@RequiredArgsConstructor
public class FxAiDailyReportService {

	private final FxRateMetricsRepository metricsRepository;
	private final FxAiDailyReportRepository reportRepository;
	private final AiClient aiClient;

	@Value("${ai.enabled:true}")
	private boolean aiEnabled;

	public void generate(LocalDate date) throws IOException {

		if (!aiEnabled)
			return;

		if (reportRepository.findByReportDate(date).isPresent()) {
			return;
		}

		List<FxRateMetrics> list = metricsRepository.findByRateDate(date);

		if (list.isEmpty())
			return;

		StringBuilder prompt = new StringBuilder("""
				以下は本日の為替統計です。
				全体傾向を3〜5文で要約してください。
				投資助言・将来予測は禁止です。

				""");

		for (FxRateMetrics m : list) {
			prompt.append("""
					%s: 終値=%s 前日比=%s%% 傾向=%s
					""".formatted(m.getQuoteCcy(), m.getCloseRate(), m.getPctPrev(), m.getTrend7d()));
		}

		String text = aiClient.generate(prompt.toString());

		reportRepository.save(FxAiDailyReport.builder().baseCcy("JPY").reportDate(date).reportText(text)
				.createdAt(OffsetDateTime.now()).build());
	}
}
