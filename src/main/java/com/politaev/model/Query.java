package com.politaev.model;

import java.time.LocalDateTime;
import java.util.UUID;

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
                -> new Query(calendarIds, duration, start, end);
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
