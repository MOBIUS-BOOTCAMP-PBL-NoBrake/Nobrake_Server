package com.example.nobrake_server.repository;

import com.example.nobrake_server.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VehicleRepository  extends JpaRepository<Vehicle, UUID> {
}
