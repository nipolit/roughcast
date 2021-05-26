package com.politaev.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Timeslot {
    private UUID id;
    private UUID calendarId;
    private UUID typeId;
    private LocalDateTime start;
    private LocalDateTime end;

    public Timeslot() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(UUID calendarId) {
        this.calendarId = calendarId;
    }

    public UUID getTypeId() {
        return typeId;
    }

    public void setTypeId(UUID typeId) {
        this.typeId = typeId;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Timeslot timeslot = (Timeslot) o;
        return id.equals(timeslot.id) && calendarId.equals(timeslot.calendarId) && typeId.equals(timeslot.typeId) && start.equals(timeslot.start) && end.equals(timeslot.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, calendarId, typeId, start, end);
    }
}
