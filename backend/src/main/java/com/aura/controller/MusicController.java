package com.aura.controller;

import com.aura.service.LastFmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/music")
@RequiredArgsConstructor
public class MusicController {

    private final LastFmService lastFmService;

    @GetMapping("/search")
    public ResponseEntity<Map> search(@RequestParam String track,
                                      @RequestParam(defaultValue = "") String artist) {
        return ResponseEntity.ok(lastFmService.searchTrack(track, artist));
    }

    @GetMapping("/info")
    public ResponseEntity<Map> getTrackInfo(@RequestParam String track,
                                            @RequestParam String artist) {
        return ResponseEntity.ok(lastFmService.getTrackInfo(track, artist));
    }

    @GetMapping("/similar")
    public ResponseEntity<Map> getSimilarTracks(@RequestParam String track,
                                                @RequestParam String artist) {
        return ResponseEntity.ok(lastFmService.getSimilarTracks(track, artist));
    }

    @GetMapping("/top")
    public ResponseEntity<Map> getTopTracks() {
        return ResponseEntity.ok(lastFmService.getTopTracks());
    }

    @GetMapping("/artist")
    public ResponseEntity<Map> getArtistInfo(@RequestParam String artist) {
        return ResponseEntity.ok(lastFmService.getArtistInfo(artist));
    }
}
