package work.techgate.fxreport.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import work.techgate.fxreport.model.FxAiDailyReport;

public interface FxAiDailyReportRepository extends JpaRepository<FxAiDailyReport, Long> {

	Optional<FxAiDailyReport> findByReportDate(LocalDate reportDate);
}
