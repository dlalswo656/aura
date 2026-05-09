package com.aura.controller;

import com.aura.service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final GeminiService geminiService;

    @GetMapping("/analyze")
    public ResponseEntity<Map<String, String>> analyzeSong(@RequestParam String track,
                                                           @RequestParam String artist) {
        String result = geminiService.analyzeSong(track, artist);
        return ResponseEntity.ok(Map.of("analysis", result));
    }

    @PostMapping("/playlist")
    public ResponseEntity<Map<String, String>> analyzePlaylist(@RequestBody List<String> songs) {
        String result = geminiService.generatePlaylistDescription(songs);
        return ResponseEntity.ok(Map.of("description", result));
    }
}
