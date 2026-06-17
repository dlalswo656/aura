package com.aura.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
@RequiredArgsConstructor
public class LastFmService {

    @Value("${lastfm.api-key}")
    private String apiKey;

    @Value("${lastfm.url}")
    private String lastFmUrl;

    private final RestTemplate restTemplate;

    public Map searchTrack(String track, String artist) {
        try {
            String encodedTrack = java.net.URLEncoder.encode(track, "UTF-8").replace("%28", "(").replace("%29", ")");
            String encodedArtist = artist != null && !artist.isEmpty()
                    ? "&artist=" + java.net.URLEncoder.encode(artist, "UTF-8").replace("+", "%20") : "";
            String url = lastFmUrl + "?method=track.search&track=" + encodedTrack
                    + encodedArtist + "&api_key=" + apiKey + "&format=json&limit=30";
            return restTemplate.getForObject(url, Map.class);
        } catch (Exception e) {
            return new java.util.HashMap<>();
        }
    }

    public Map getTrackInfo(String track, String artist) {
        String url = UriComponentsBuilder.fromHttpUrl(lastFmUrl)
                .queryParam("method", "track.getInfo")
                .queryParam("track", track)
                .queryParam("artist", artist)
                .queryParam("api_key", apiKey)
                .queryParam("format", "json")
                .toUriString();

        return restTemplate.getForObject(url, Map.class);
    }

    public Map getSimilarTracks(String track, String artist) {
        String url = UriComponentsBuilder.fromHttpUrl(lastFmUrl)
                .queryParam("method", "track.getSimilar")
                .queryParam("track", track)
                .queryParam("artist", artist)
                .queryParam("api_key", apiKey)
                .queryParam("format", "json")
                .queryParam("limit", 5)
                .toUriString();

        return restTemplate.getForObject(url, Map.class);
    }

    public Map getArtistInfo(String artist) {
        String url = UriComponentsBuilder.fromHttpUrl(lastFmUrl)
                .queryParam("method", "artist.getInfo")
                .queryParam("artist", artist)
                .queryParam("api_key", apiKey)
                .queryParam("format", "json")
                .toUriString();

        return restTemplate.getForObject(url, Map.class);
    }

    public Map getTopTracks() {
        String url = UriComponentsBuilder.fromHttpUrl(lastFmUrl)
                .queryParam("method", "chart.getTopTracks")
                .queryParam("api_key", apiKey)
                .queryParam("format", "json")
                .queryParam("limit", 20)
                .toUriString();

        return restTemplate.getForObject(url, Map.class);
    }
}
