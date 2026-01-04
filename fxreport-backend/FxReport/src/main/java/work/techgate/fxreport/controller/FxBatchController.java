package work.techgate.fxreport.controller;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import work.techgate.fxreport.service.FxBatchService;

@RestController
@RequestMapping("/api/batch")
@RequiredArgsConstructor
public class FxBatchController {

	private final FxBatchService fxBatchService;

	@PostMapping("/run")
	public void run(@RequestParam LocalDate date) throws IOException {
		fxBatchService.run(date);
	}
}
