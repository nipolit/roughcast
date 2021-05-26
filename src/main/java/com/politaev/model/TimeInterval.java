package com.politaev.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class TimeInterval {
    private final LocalDateTime start;
    private final LocalDateTime end;

    public TimeInterval(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    public boolean notShorter(int durationMinutes) {
        return Duration.between(start, end).toMinutes() >= durationMinutes;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }
}
