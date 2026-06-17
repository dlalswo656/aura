package com.aura.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GeminiService {

    @Value("${gemini.api-key}")
    private String apiKey;

    @Value("${gemini.url}")
    private String geminiUrl;

    private final RestTemplate restTemplate;

    public String analyzeSong(String title, String artist) {
        String extraContext = getExtraContext(title);
        String prompt = String.format(
            "%s음악 '%s' by %s 에 대해 다음을 한국어로 분석해줘:\n" +
            "1. 곡의 전체적인 분위기와 감정\n" +
            "2. 어울리는 상황 (예: 드라이브, 새벽 감성, 운동 등)\n" +
            "3. 이 곡의 매력 포인트 (피처링/참여 아티스트가 있다면 반드시 언급해줘)\n" +
            "4. 비슷한 분위기의 추천 곡 3개 (아티스트 - 곡명 형식)\n\n" +
            "간결하고 자연스럽게 작성해줘.",
            extraContext, title, artist
        );
        return callGemini(prompt);
    }

    // DNA (More Than A Game) 전용 프롬프트
    private String getExtraContext(String title) {
        if (title != null && title.toLowerCase().contains("dna") && title.toLowerCase().contains("more than a game")) {
            return "[곡 배경 정보] 이 곡은 2026 FIFA 월드컵 공식 주제가로, " +
                   "대한민국 작곡가이자 가수 이재(EJAE)가 세계적인 성악가 테너 안드레아 보첼리(Andrea Bocelli), " +
                   "데이비드 게타(David Guetta), 메건 디 스탤리온(Megan Thee Stallion)과 함께 부른 곡이다. " +
                   "가사에 한국어가 포함되어 있는 것이 큰 포인트다. 이 정보를 분석에 반드시 반영해줘.\n\n";
        }
        return "";
    }

    public String generatePlaylistDescription(List<String> songs) {
        String songList = String.join(", ", songs);
        String prompt = String.format(
            "다음 곡들로 구성된 플레이리스트의 분위기를 한국어로 분석해줘: %s\n" +
            "1. 이 플레이리스트의 전체적인 컨셉\n" +
            "2. 어울리는 상황\n" +
            "3. 플레이리스트 추천 이름 3개\n\n" +
            "감성적이고 자연스럽게 작성해줘.",
            songList
        );
        return callGemini(prompt);
    }

    private String callGemini(String prompt) {
        String url = geminiUrl + "?key=" + apiKey;

        Map<String, Object> part = Map.of("text", prompt);
        Map<String, Object> content = Map.of("parts", List.of(part));
        Map<String, Object> body = Map.of("contents", List.of(content));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            return extractText(response.getBody());
        } catch (Exception e) {
            return "AI 분석을 불러오는 데 실패했습니다.";
        }
    }

    @SuppressWarnings("unchecked")
    private String extractText(Map response) {
        try {
            List<Map> candidates = (List<Map>) response.get("candidates");
            Map content = (Map) candidates.get(0).get("content");
            List<Map> parts = (List<Map>) content.get("parts");
            return (String) parts.get(0).get("text");
        } catch (Exception e) {
            return "분석 결과를 파싱하는 데 실패했습니다.";
        }
    }
}
