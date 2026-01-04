package work.techgate.fxreport.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import work.techgate.fxreport.model.FxAiComment;

public interface FxAiCommentRepository extends JpaRepository<FxAiComment, Long> {

	Optional<FxAiComment> findByAndBaseCcyAndQuoteCcyAndRateDate(String baseCcy, String quoteCcy, LocalDate rateDate);
}
