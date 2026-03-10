package com.example.nobrake_server.repository;

import com.example.nobrake_server.entity.VehicleEcu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VehicleEcuRepository extends JpaRepository<VehicleEcu, UUID> {
}
