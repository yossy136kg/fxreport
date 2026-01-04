package work.techgate.fxreport.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import work.techgate.fxreport.dto.RateDataDto;
import work.techgate.fxreport.model.FxAiComment;
import work.techgate.fxreport.model.FxRateMetrics;
import work.techgate.fxreport.repository.FxAiCommentRepository;
import work.techgate.fxreport.repository.FxRateMetricsRepository;
import work.techgate.fxreport.service.FxRateService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/fx")
@RequiredArgsConstructor
public class FxSummaryController {

	private final FxRateMetricsRepository fxRateMetricsRepository;
	private final FxAiCommentRepository fxAiCommentRepository;
	private final FxRateService fxRateService;

	@GetMapping("/summary")
	public List<RateDataDto> getSummary(
			@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		List<FxRateMetrics> metrics = fxRateMetricsRepository.findByRateDate(date);

		return metrics.stream().map(m -> {
			String ccy = m.getQuoteCcy();
			String aiSummary = fxAiCommentRepository.findByAndBaseCcyAndQuoteCcyAndRateDate(m.getBaseCcy(), ccy, date)
					.map(FxAiComment::getSummary).orElse("");
			return new RateDataDto(m.getBaseCcy(), m.getCloseRate(), m.getDiffPrev(), m.getPctPrev(), m.getMa7(),
					fxRateService.getLast7DaysRates(m.getBaseCcy(), ccy, date), aiSummary);
		}).toList();
	}
}
