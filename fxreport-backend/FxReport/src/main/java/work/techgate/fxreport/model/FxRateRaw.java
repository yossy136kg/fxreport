package work.techgate.fxreport.model;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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
@Table(name = "fx_rate_raw", uniqueConstraints = { @UniqueConstraint(columnNames = { "base_ccy", "rate_date" }) })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FxRateRaw {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "base_ccy", nullable = false, length = 3)
	private String baseCcy;

	@Column(name = "rate_date", nullable = false)
	private LocalDate rateDate;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "jsonb", nullable = false)
	private String payload;

	@Column(name = "fetched_at", nullable = false)
	private OffsetDateTime fetchedAt;
}
