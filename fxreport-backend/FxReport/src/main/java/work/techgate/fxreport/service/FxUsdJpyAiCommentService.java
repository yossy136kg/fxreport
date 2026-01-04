package work.techgate.fxreport.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.OffsetDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import work.techgate.fxreport.ai.AiClient;
import work.techgate.fxreport.model.FxAiComment;
import work.techgate.fxreport.model.FxRateMetrics;
import work.techgate.fxreport.repository.FxAiCommentRepository;
import work.techgate.fxreport.repository.FxRateMetricsRepository;

@Service
@RequiredArgsConstructor
public class FxUsdJpyAiCommentService {

	private final FxRateMetricsRepository fxRateMetricsRepository;
	private final FxAiCommentRepository fxAiCommentRepository;
	private final AiClient aiClient;

	@Value("${ai.enabled:true}")
	private boolean aiEnabled;

	public void generate(LocalDate date) throws IOException {

		if (!aiEnabled)
			return;

		// USD/JPY only
		FxRateMetrics m = fxRateMetricsRepository.findByBaseCcyAndQuoteCcyAndRateDate("USD", "JPY", date).orElse(null);

		if (m == null)
			return;

		boolean exists = fxAiCommentRepository.findByAndBaseCcyAndQuoteCcyAndRateDate("USD", "JPY", date).isPresent();

		if (exists)
			return;

		String prompt = """
				以下はUSD/JPYの為替統計です。
				事実のみを2〜3文で日本語要約してください。
				投資助言や将来予測は禁止です。

				終値: %s
				前日差: %s
				前日比: %s%%
				7日平均: %s
				傾向: %s
				""".formatted(m.getCloseRate(), m.getDiffPrev(), m.getPctPrev(), m.getMa7(), m.getTrend7d());

		String text = aiClient.generate(prompt);

		fxAiCommentRepository.save(FxAiComment.builder().baseCcy("USD").quoteCcy("JPY").rateDate(date).summary(text)
				.createdAt(OffsetDateTime.now()).build());
	}
}
