package work.techgate.fxreport.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import work.techgate.fxreport.model.FxRateMetrics;

public interface FxRateMetricsRepository extends JpaRepository<FxRateMetrics, Long> {

	Optional<FxRateMetrics> findByBaseCcyAndQuoteCcyAndRateDate(String baseCcy, String quoteCcy, LocalDate rateDate);

	List<FxRateMetrics> findByRateDate(LocalDate rateDate);
}
