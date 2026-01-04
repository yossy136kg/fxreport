package work.techgate.fxreport.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import work.techgate.fxreport.model.FxRate;

public interface FxRateRepository extends JpaRepository<FxRate, Long> {

	Optional<FxRate> findByBaseCcyAndQuoteCcyAndRateDate(String baseCcy, String quoteCcy, LocalDate rateDate);

	List<FxRate> findByBaseCcyAndRateDate(String baseCcy, LocalDate rateDate);

	List<FxRate> findByBaseCcyAndQuoteCcyOrderByRateDateAsc(String baseCcy, String quoteCcy);

	List<FxRate> findByBaseCcyAndQuoteCcyAndRateDateBetweenOrderByRateDateAsc(String baseCcy, String quoteCcy,
			LocalDate startDate, LocalDate endDate);
}
