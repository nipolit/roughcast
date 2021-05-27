package com.politaev.roughcast.model;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Stream;

public class Event {
    private final LocalDateTime dateTime;
    private final UUID calendarId;
    private final EventType type;

    public static Stream<Event> fromTimeslot(Timeslot timeslot) {
        return Stream.of(
                new Event(timeslot.getStart(), timeslot.getCalendarId(), EventType.TIMESLOT_START),
                new Event(timeslot.getEnd(), timeslot.getCalendarId(), EventType.TIMESLOT_END)
        );
    }

    public static Stream<Event> fromAppointment(Appointment appointment) {
        return Stream.of(
                new Event(appointment.getStart(), appointment.getCalendarId(), EventType.APPOINTMENT_START),
                new Event(appointment.getEnd(), appointment.getCalendarId(), EventType.APPOINTMENT_END)
        );
    }

    private Event(LocalDateTime dateTime, UUID calendarId, EventType type) {
        this.dateTime = dateTime;
        this.calendarId = calendarId;
        this.type = type;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public UUID getCalendarId() {
        return calendarId;
    }

    public EventType getType() {
        return type;
    }

    public enum EventType {
        TIMESLOT_START,
        TIMESLOT_END,
        APPOINTMENT_START,
        APPOINTMENT_END,
    }
}
