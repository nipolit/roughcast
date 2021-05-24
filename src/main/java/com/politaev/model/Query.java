package com.politaev.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;

public class Query {
    private final UUID[] calendarIds;
    private final int duration;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final UUID timeslotType;

    public static AddCalendarIds buildQuery() {
        return calendarIds
                -> duration
                -> start
                -> end
                -> {
            requireValidInput(calendarIds, duration, start, end);
            return new Query(calendarIds, duration, start, end);
        };
    }

    private static void requireValidInput(UUID[] calendarIds, int duration, LocalDateTime start, LocalDateTime end) {
        requireValidCalendarIds(calendarIds);
        requirePositiveDuration(duration);
        requireNonNull(start, "start must not be null");
        requireNonNull(end, "end must not be null");
        requireStartBeforeEnd(start, end);
        requireTimeIntervalLongerThanDuration(duration, start, end);
    }

    private static void requireValidCalendarIds(UUID[] calendarIds) {
        requireNonNull(calendarIds, "calendarIds must not be null");
        if (calendarIds.length == 0) {
            throw new IllegalArgumentException("calendarIds must not be empty");
        }
        if (stream(calendarIds).anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("All calendarIds must not be null");
        }
    }

    private static void requirePositiveDuration(int duration) {
        if (duration <= 0) {
            throw new IllegalArgumentException("duration must be a positive number");
        }
    }

    private static void requireStartBeforeEnd(LocalDateTime start, LocalDateTime end) {
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("start must be before end");
        }
    }

    private static void requireTimeIntervalLongerThanDuration(int duration, LocalDateTime start, LocalDateTime end) {
        if (Duration.between(start, end).toMinutes() < duration) {
            throw new IllegalArgumentException("duration must fit into the specified time interval");
        }
    }

    private Query(UUID[] calendarIds, int duration, LocalDateTime start, LocalDateTime end) {
        this.calendarIds = calendarIds;
        this.duration = duration;
        this.start = start;
        this.end = end;
        this.timeslotType = null;
    }

    private Query(Query original, UUID timeslotType) {
        this.calendarIds = original.getCalendarIds();
        this.duration = original.getDuration();
        this.start = original.getStart();
        this.end = original.getEnd();
        this.timeslotType = timeslotType;
    }

    public UUID[] getCalendarIds() {
        return calendarIds;
    }

    public int getDuration() {
        return duration;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public UUID getTimeslotType() {
        return timeslotType;
    }

    public Query withTimeslotType(UUID timeslotType) {
        return new Query(this, timeslotType);
    }

    public interface AddCalendarIds {
        AddDuration withCalendarIds(UUID[] calendarIds);
    }

    public interface AddDuration {
        AddStart withDurationMinutes(int duration);
    }

    public interface AddStart {
        AddEnd withStart(LocalDateTime start);
    }

    public interface AddEnd {
        Query withEnd(LocalDateTime end);
    }
}
