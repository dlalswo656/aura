package com.aura.repository;

import com.aura.entity.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
    List<SearchHistory> findByUserIdOrderBySearchedAtDesc(Long userId);

    @Modifying
    @Query("DELETE FROM SearchHistory s WHERE s.user.id = :userId")
    void deleteAllByUserId(Long userId);
}
