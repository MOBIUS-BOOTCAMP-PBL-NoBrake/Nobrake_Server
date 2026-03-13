package com.example.nobrake_server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleEcu {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "ecu_id")
    private Ecu ecu;

    public static VehicleEcu of(Vehicle vehicle, Ecu ecu) {
        return VehicleEcu.builder()
                .vehicle(vehicle)
                .ecu(ecu)
                .build();
    }
}
