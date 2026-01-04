package work.techgate.fxreport.service;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import work.techgate.fxreport.model.FxRateRaw;
import work.techgate.fxreport.repository.FxRateRawRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class FrankfurterApiService {

	private static final String BASE_URL = "https://api.frankfurter.dev/v1";

	private final FxRateRawRepository fxRateRawRepository;
	private final ObjectMapper objectMapper;
	private final RestTemplate restTemplate;
	String quoteCurrency = "JPY";

	public FxRateRaw fetchByDate(String baseCcy, LocalDate date) {
		String url = BASE_URL + "/" + date + "?base=" + baseCcy + "&to=" + quoteCurrency;
		return fetchAndSave(url, baseCcy);
	}

	/**
	 * API呼び出し＋DB保存の共通処理
	 */
	private FxRateRaw fetchAndSave(String url, String baseCcy) {
		log.info("URL:" + url);
		try {
			String response = restTemplate.getForObject(url, String.class);
			log.debug("response:" + response);

			// レスポンスから date を抜く
			String rateDate = objectMapper.readTree(response).get("date").asText();

			LocalDate parsedDate = LocalDate.parse(rateDate);

			// 既に存在する場合は再取得しない
			return fxRateRawRepository.findByBaseCcyAndRateDate(baseCcy, parsedDate)
					.orElseGet(() -> fxRateRawRepository.save(FxRateRaw.builder().baseCcy(baseCcy).rateDate(parsedDate)
							.payload(response).fetchedAt(OffsetDateTime.now()).build()));

		} catch (Exception e) {
			throw new RuntimeException("Frankfurter API fetch failed: " + url, e);
		}
	}
}
