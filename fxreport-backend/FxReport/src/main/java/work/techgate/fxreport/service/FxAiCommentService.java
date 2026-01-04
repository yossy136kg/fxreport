package work.techgate.fxreport.service;

import java.io.IOException;
import java.time.OffsetDateTime;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import work.techgate.fxreport.ai.AiClient;
import work.techgate.fxreport.model.FxAiComment;
import work.techgate.fxreport.model.FxRateMetrics;
import work.techgate.fxreport.repository.FxAiCommentRepository;

@Service
@RequiredArgsConstructor
public class FxAiCommentService {

	private final AiClient aiClient;
	private final FxAiCommentRepository fxAiCommentRepository;

	public void generate(FxRateMetrics metrics) throws IOException {

		// 既に存在するなら再生成しない
		boolean exists = fxAiCommentRepository.findByAndBaseCcyAndQuoteCcyAndRateDate(metrics.getBaseCcy(),
				metrics.getQuoteCcy(), metrics.getRateDate()).isPresent();

		if (exists) {
			return;
		}

		String prompt = buildPrompt(metrics);
		String summary = aiClient.generate(prompt);

		FxAiComment comment = FxAiComment.builder().baseCcy(metrics.getBaseCcy()).quoteCcy(metrics.getQuoteCcy())
				.rateDate(metrics.getRateDate()).summary(summary).createdAt(OffsetDateTime.now()).build();

		fxAiCommentRepository.save(comment);
	}

	private String buildPrompt(FxRateMetrics m) {
		return """
				あなたは為替データを要約するアナリストです。
				以下の数値をもとに、事実のみを簡潔に日本語で説明してください。

				【条件】
				- 投資助言は禁止
				- 将来予測は禁止
				- 2〜3文
				- 客観的な表現

				【データ】
				通貨ペア: %s/JPY
				終値: %s
				前日差: %s
				前日比(%%): %s
				7日移動平均: %s
				直近7日の傾向: %s
				""".formatted(m.getQuoteCcy(), m.getCloseRate(), m.getDiffPrev(), m.getPctPrev(), m.getMa7(),
				m.getTrend7d());
	}
}
