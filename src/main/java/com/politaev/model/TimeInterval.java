package com.politaev.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class TimeInterval {
    private final LocalDateTime start;
    private final LocalDateTime end;

    public TimeInterval(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    public static TimeInterval parse(String timeIntervalString) {
        Objects.requireNonNull(timeIntervalString);
        String[] startAndEnd = timeIntervalString.split("/");
        if (startAndEnd.length != 2) {
            throw new IllegalArgumentException("Unable to parse the time interval");
        }
        var start = LocalDateTime.parse(startAndEnd[0]);
        var end = LocalDateTime.parse(startAndEnd[1]);
        return new TimeInterval(start, end);
    }

    public boolean notShorter(int durationMinutes) {
        return Duration.between(start, end).toMinutes() >= durationMinutes;
    }

    public boolean isZeroLength() {
        return start.equals(end);
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return start + " - " + end;
    }
}
