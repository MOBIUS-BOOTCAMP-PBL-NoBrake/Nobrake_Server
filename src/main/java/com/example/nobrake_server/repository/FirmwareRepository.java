package com.example.nobrake_server.repository;

import com.example.nobrake_server.entity.Firmware;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FirmwareRepository extends JpaRepository<Firmware, UUID> {
    Optional<Firmware> findByEcu_TargetEcuIdAndVersion(Integer targetEcuId, Integer version);
}
