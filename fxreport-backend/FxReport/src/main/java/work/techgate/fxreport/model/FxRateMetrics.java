package work.techgate.fxreport.model;

import java.math.BigDecimal;
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
@Table(name = "fx_rate_metrics", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "base_ccy", "quote_ccy", "rate_date" }) })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FxRateMetrics {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "base_ccy", nullable = false, length = 3)
	private String baseCcy;

	@Column(name = "quote_ccy", nullable = false, length = 3)
	private String quoteCcy;

	@Column(name = "rate_date", nullable = false)
	private LocalDate rateDate;

	@Column(name = "close_rate", nullable = false, precision = 18, scale = 6)
	private BigDecimal closeRate;

	@Column(name = "diff_prev", precision = 18, scale = 6)
	private BigDecimal diffPrev;

	@Column(name = "pct_prev", precision = 12, scale = 6)
	private BigDecimal pctPrev;

	@Column(name = "ma7", precision = 18, scale = 6)
	private BigDecimal ma7;

	@Column(name = "trend_7d", length = 20)
	private String trend7d; // UP / DOWN / FLAT

	@Column(name = "created_at", nullable = false)
	private OffsetDateTime createdAt;
}
