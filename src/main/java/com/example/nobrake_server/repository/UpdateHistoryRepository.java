package com.example.nobrake_server.repository;

import com.example.nobrake_server.entity.UpdateHistory;
import com.example.nobrake_server.entity.UpdateStatus;
import com.example.nobrake_server.entity.VehicleEcu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UpdateHistoryRepository extends JpaRepository<UpdateHistory, UUID> {

    Optional<UpdateHistory> findFirstByEcuVehicleAndFirmware_VersionAndStatusOrderByUpdateDateDesc(VehicleEcu mapping, Integer swVersion, UpdateStatus updateStatus);
}
