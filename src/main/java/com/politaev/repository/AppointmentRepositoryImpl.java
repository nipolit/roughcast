package com.politaev.repository;

import com.politaev.model.Appointment;
import com.politaev.model.Query;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableNavigableMap;

public class AppointmentRepositoryImpl implements AppointmentRepository {
    private final NavigableMap<LocalDateTime, Appointment> appointmentsByStart;
    private final NavigableMap<LocalDateTime, Appointment> appointmentsByEnd;

    public AppointmentRepositoryImpl(List<StoredData> storedDataList) {
        NavigableMap<LocalDateTime, Appointment> appointmentsByStart = new TreeMap<>();
        NavigableMap<LocalDateTime, Appointment> appointmentsByEnd = new TreeMap<>();
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
        Set<Appointment> endAfterIntervalStartSet = new HashSet<>(endAfterIntervalStart);
        Set<UUID> requestedCalendars = new HashSet<>(Arrays.asList(query.getCalendarIds()));
        return startBeforeIntervalEnd.stream()
                .filter(endAfterIntervalStartSet::contains)
                .filter(appointment -> requestedCalendars.contains(appointment.getCalendarId()))
                .collect(Collectors.toList());
    }
}
