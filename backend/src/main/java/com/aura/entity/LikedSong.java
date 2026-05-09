package com.aura.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "liked_songs")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikedSong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 200)
    private String trackName;

    @Column(nullable = false, length = 200)
    private String artistName;

    @Column(length = 500)
    private String imageUrl;

    @Column(length = 500)
    private String lastFmUrl;

    @Column(updatable = false)
    private LocalDateTime likedAt;

    @PrePersist
    protected void onCreate() {
        likedAt = LocalDateTime.now();
    }
}
