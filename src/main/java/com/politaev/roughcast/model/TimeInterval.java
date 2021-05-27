package com.politaev.roughcast.model;

import org.threeten.extra.Interval;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimeInterval {
    private final LocalDateTime start;
    private final LocalDateTime end;

    public TimeInterval(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

    public static TimeInterval parse(String timeIntervalString) {
        var interval = Interval.parse(timeIntervalString);
        var startInstant = interval.getStart();
        var endInstant = interval.getEnd();
        var start = LocalDateTime.ofInstant(startInstant, ZoneId.systemDefault());
        var end = LocalDateTime.ofInstant(endInstant, ZoneId.systemDefault());
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
