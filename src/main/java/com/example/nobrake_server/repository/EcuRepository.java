package com.example.nobrake_server.repository;

import com.example.nobrake_server.entity.Ecu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EcuRepository extends JpaRepository<Ecu, UUID> {
}
