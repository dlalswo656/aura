package com.aura.repository;

import com.aura.entity.LikedSong;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface LikedSongRepository extends JpaRepository<LikedSong, Long> {
    List<LikedSong> findByUserIdOrderByLikedAtDesc(Long userId);
    Optional<LikedSong> findByUserIdAndTrackNameAndArtistName(Long userId, String trackName, String artistName);
    boolean existsByUserIdAndTrackNameAndArtistName(Long userId, String trackName, String artistName);
    void deleteByUserIdAndTrackNameAndArtistName(Long userId, String trackName, String artistName);
}
