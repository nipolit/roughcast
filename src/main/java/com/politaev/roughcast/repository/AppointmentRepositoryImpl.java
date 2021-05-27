package com.politaev.roughcast.repository;

import com.politaev.roughcast.model.Appointment;
import com.politaev.roughcast.model.Query;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableNavigableMap;

public class AppointmentRepositoryImpl implements AppointmentRepository {
    private final NavigableMap<LocalDateTime, Appointment> appointmentsByStart;
    private final NavigableMap<LocalDateTime, Appointment> appointmentsByEnd;

    public AppointmentRepositoryImpl(List<StoredData> storedDataList) {
        var appointmentsByStart = new TreeMap<LocalDateTime, Appointment>();
        var appointmentsByEnd = new TreeMap<LocalDateTime, Appointment>();
        for (StoredData storedData : storedDataList) {
            for (Appointment appointment : storedData.getAppointments()) {
                appointmentsByStart.put(appointment.getStart(), appointment);
                appointmentsByEnd.put(appointment.getEnd(), appointment);
            }
        }
        this.appointmentsByStart = unmodifiableNavigableMap(appointmentsByStart);
        this.appointmentsByEnd = unmodifiableNavigableMap(appointmentsByEnd);
    }

    @Override
    public List<Appointment> findAppointments(Query query) {
        var startBeforeIntervalEnd = appointmentsByStart.headMap(query.getEnd(), false).values();
        var endAfterIntervalStart = appointmentsByEnd.tailMap(query.getStart(), false).values();
        var endAfterIntervalStartSet = new HashSet<>(endAfterIntervalStart);
        var requestedCalendars = new HashSet<>(Arrays.asList(query.getCalendarIds()));
        return startBeforeIntervalEnd.stream()
                .filter(endAfterIntervalStartSet::contains)
                .filter(appointment -> requestedCalendars.contains(appointment.getCalendarId()))
                .collect(Collectors.toList());
    }
}
