package com.example.trainingapp.Domain;

import java.time.LocalDateTime;

public record Activity(
        String name,
        LocalDateTime date,
        double distance,
        double time,
        ActivityType type,
        String description) {
}
