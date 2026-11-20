package com.noticewise.service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;
@Service
public class NoticeAnalysisService {
    @Value("${gemini.api-key:}") private String apiKey;
    @Value("${gemini.model:gemini-2.5-flash}") private String model;
    private final RestTemplate rest = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    public Map<String,String> analyze(String noticeText) {
        if (apiKey != null && !apiKey.isBlank()) {
            try { return analyzeWithGemini(noticeText); }
            catch (Exception ex) { return fallback(noticeText, "Gemini request failed; showing a safe fallback. Check your API key/model and try again."); }
        }
        return fallback(noticeText, "Gemini API key is not configured yet. Add GEMINI_API_KEY to enable AI analysis.");
    }

    private Map<String,String> analyzeWithGemini(String text) throws Exception {
        String prompt = """
          You are NoticeWise, an assistant that explains notices clearly.
          Analyze the notice below and return ONLY valid JSON with exactly these keys:
          title, simpleSummary, deadlines, eligibility, requiredDocuments, actionChecklist.
          Use concise plain language. If a detail is absent, say "Not specified in the notice".
          Never invent dates, eligibility rules, or required documents. Mention that users should verify
          important information against the original notice.
          actionChecklist should be a short numbered list as plain text.
          NOTICE:
          """ + text;
        Map<String,Object> body = Map.of(
            "contents", List.of(Map.of("parts", List.of(Map.of("text", prompt)))),
            "generationConfig", Map.of("temperature", 0.2, "responseMimeType", "application/json")
        );
        String url = "https://generativelanguage.googleapis.com/v1beta/models/" + model + ":generateContent?key=" + apiKey;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String raw = rest.postForEntity(url, new HttpEntity<>(body, headers), String.class).getBody();
        JsonNode root = mapper.readTree(raw);
        String answer = root.path("candidates").path(0).path("content").path("parts").path(0).path("text").asText();
        JsonNode json = mapper.readTree(answer);
        Map<String,String> result = new LinkedHashMap<>();
        for (String key : List.of("title","simpleSummary","deadlines","eligibility","requiredDocuments","actionChecklist")) {
            result.put(key, json.path(key).asText("Not specified in the notice."));
        }
        return result;
    }

    private Map<String,String> fallback(String text, String note) {
        String compact = text.replaceAll("\\s+", " ").trim();
        String title = compact.length() > 65 ? compact.substring(0,65) + "…" : compact;
        Map<String,String> result = new LinkedHashMap<>();
        result.put("title", title.isBlank() ? "Untitled notice" : title);
        result.put("simpleSummary", note + "\n\nNotice preview: " +
            (compact.length() > 450 ? compact.substring(0,450) + "…" : compact));
        result.put("deadlines", "Not automatically extracted in offline mode. Check the original notice.");
        result.put("eligibility", "Please review the original notice for eligibility conditions.");
        result.put("requiredDocuments", "Please review the original notice for required documents.");
        result.put("actionChecklist", "1. Read the complete original notice.\n2. Verify dates and eligibility with the issuing authority.\n3. Prepare only the documents mentioned in the notice.\n4. Ask the issuing department if anything is unclear.");
        return result;
    }
}
