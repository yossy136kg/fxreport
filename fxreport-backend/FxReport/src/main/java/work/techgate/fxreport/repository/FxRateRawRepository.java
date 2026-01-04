package work.techgate.fxreport.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import work.techgate.fxreport.model.FxRateRaw;

public interface FxRateRawRepository extends JpaRepository<FxRateRaw, Long> {

	Optional<FxRateRaw> findByBaseCcyAndRateDate(String baseCcy, LocalDate rateDate);
}
