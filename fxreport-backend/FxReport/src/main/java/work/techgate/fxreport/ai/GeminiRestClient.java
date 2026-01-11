package work.techgate.fxreport.ai;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class GeminiRestClient implements AiClient {

	private final RestTemplate restTemplate;

	@Value("${gemini.api.key}")
	private String apiKey;

	@Value("${gemini.model}")
	private String model;

	@Value("${gemini.api.base}")
	private String baseUrl;

	@Override
	public String generate(String prompt) {
		// POST
		// https://generativelanguage.googleapis.com/v1beta/models/{model}:generateContent?key=...
		String url = baseUrl + "/models/" + model + ":generateContent?key=" + apiKey;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// Gemini形式: { contents: [ { parts: [ { text } ] } ] }
		Map<String, Object> body = Map.of("contents", List.of(Map.of("parts", List.of(Map.of("text", prompt)))),
				// 任意：出力を安定させる
				"generationConfig", Map.of("temperature", 0.4, "maxOutputTokens", 2048));

		HttpEntity<Map<String, Object>> req = new HttpEntity<>(body, headers);

		ResponseEntity<Map> res = restTemplate.postForEntity(url, req, Map.class);
		Map<String, Object> json = res.getBody();
		return extractText(json);
	}

	@SuppressWarnings("unchecked")
	private String extractText(Map<String, Object> json) {
		if (json == null)
			return "";

		// candidates[0].content.parts[0].text を取り出す（試すだけならこれで十分）
		Object candidatesObj = json.get("candidates");
		if (!(candidatesObj instanceof List<?> candidates) || candidates.isEmpty())
			return "";

		Object cand0Obj = candidates.get(0);
		if (!(cand0Obj instanceof Map<?, ?> cand0))
			return "";

		Object contentObj = cand0.get("content");
		if (!(contentObj instanceof Map<?, ?> content))
			return "";

		Object partsObj = content.get("parts");
		if (!(partsObj instanceof List<?> parts) || parts.isEmpty())
			return "";

		Object part0Obj = parts.get(0);
		if (!(part0Obj instanceof Map<?, ?> part0))
			return "";

		Object textObj = part0.get("text");
		return textObj instanceof String s ? s : "";
	}
}
