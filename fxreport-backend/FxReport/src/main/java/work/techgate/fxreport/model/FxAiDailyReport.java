package work.techgate.fxreport.model;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fx_ai_daily_report", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "base_ccy", "report_date" }) })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FxAiDailyReport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "base_ccy", nullable = false, length = 3)
	private String baseCcy;

	@Column(name = "report_date", nullable = false)
	private LocalDate reportDate;

	@Column(name = "report_text", nullable = false, columnDefinition = "text")
	private String reportText;

	@Column(name = "created_at", nullable = false)
	private OffsetDateTime createdAt;
}
