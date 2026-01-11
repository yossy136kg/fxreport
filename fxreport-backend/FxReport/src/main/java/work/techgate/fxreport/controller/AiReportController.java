package work.techgate.fxreport.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import work.techgate.fxreport.model.FxAiDailyReport;
import work.techgate.fxreport.repository.FxAiDailyReportRepository;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
public class AiReportController {

	private final FxAiDailyReportRepository fxAiDailyReportRepository;

	@GetMapping("/report")
	public ResponseEntity<String> getAiReport(
			@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		return fxAiDailyReportRepository.findByReportDate(date).map(FxAiDailyReport::getReportText)
				.map(ResponseEntity::ok).orElse(ResponseEntity.ok(""));
	}
}
