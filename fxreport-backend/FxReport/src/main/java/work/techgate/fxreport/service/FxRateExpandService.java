package work.techgate.fxreport.service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Iterator;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import work.techgate.fxreport.model.FxRate;
import work.techgate.fxreport.model.FxRateRaw;
import work.techgate.fxreport.repository.FxRateRepository;

@Service
@RequiredArgsConstructor
public class FxRateExpandService {

	private final FxRateRepository fxRateRepository;
	private final ObjectMapper objectMapper;

	/**
	 * fx_rate_raw → fx_rate 展開
	 */
	@Transactional
	public void expand(FxRateRaw raw) {

		try {
			JsonNode root = objectMapper.readTree(raw.getPayload());
			JsonNode ratesNode = root.get("rates");

			if (ratesNode == null || !ratesNode.isObject()) {
				throw new IllegalStateException("rates node not found or invalid");
			}

			Iterator<Map.Entry<String, JsonNode>> fields = ratesNode.fields();

			while (fields.hasNext()) {
				Map.Entry<String, JsonNode> entry = fields.next();

				String quoteCcy = entry.getKey();
				BigDecimal rate = entry.getValue().decimalValue();

				// 既に存在する場合はスキップ
				boolean exists = fxRateRepository
						.findByBaseCcyAndQuoteCcyAndRateDate(raw.getBaseCcy(), quoteCcy, raw.getRateDate()).isPresent();

				if (exists) {
					continue;
				}

				FxRate fxRate = FxRate.builder().baseCcy(raw.getBaseCcy()).quoteCcy(quoteCcy)
						.rateDate(raw.getRateDate()).rate(rate).createdAt(OffsetDateTime.now()).build();

				fxRateRepository.save(fxRate);
			}

		} catch (Exception e) {
			throw new RuntimeException("Failed to expand fx_rate from payload", e);
		}
	}
}
