package com.example.nobrake_server.repository;

import com.example.nobrake_server.entity.UpdateHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UpdateHistoryRepository extends JpaRepository<UpdateHistory, Long> {
}
