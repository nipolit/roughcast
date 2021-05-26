package com.politaev.repository;

import com.politaev.model.Appointment;
import com.politaev.model.Query;
import com.politaev.model.Timeslot;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableMap;

public class TimeslotRepositoryImpl implements TimeslotRepository {
    private final Map<UUID, NavigableMap<LocalDateTime, Timeslot>> timeslotsByStartByCalendar;
    private final Map<UUID, NavigableMap<LocalDateTime, Timeslot>> timeslotsByEndByCalendar;

    public TimeslotRepositoryImpl(List<StoredData> storedDataList) {
        Map<UUID, NavigableMap<LocalDateTime, Timeslot>> timeslotsByStartByCalendar = new HashMap<>();
        Map<UUID, NavigableMap<LocalDateTime, Timeslot>> timeslotsByEndByCalendar = new HashMap<>();
        for (StoredData storedData : storedDataList) {
            for (Timeslot timeslot : storedData.getTimeslots()) {
                var timeslotsByStart = timeslotsByStartByCalendar.computeIfAbsent(timeslot.getCalendarId(), k -> new TreeMap<>());
                var timeslotsByEnd = timeslotsByEndByCalendar.computeIfAbsent(timeslot.getCalendarId(), k -> new TreeMap<>());
                timeslotsByStart.put(timeslot.getStart(), timeslot);
                timeslotsByEnd.put(timeslot.getEnd(), timeslot);
            }
        }
        this.timeslotsByStartByCalendar = unmodifiableMap(timeslotsByStartByCalendar);
        this.timeslotsByEndByCalendar = unmodifiableMap(timeslotsByEndByCalendar);
    }

    @Override
    public Map<UUID, List<Timeslot>> findTimeslotsForUsers(Query query) {
        return Arrays.stream(query.getCalendarIds())
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                calendarId -> findTimeslotsForCalendar(calendarId, query)
                        )
                );
    }

    private List<Timeslot> findTimeslotsForCalendar(UUID calendarId, Query query) {
        var timeslotsByStart = timeslotsByStartByCalendar.get(calendarId);
        var timeslotsByEnd = timeslotsByEndByCalendar.get(calendarId);
        var startBeforeIntervalEnd = timeslotsByStart.headMap(query.getEnd(), false).values();
        var endAfterIntervalStart = timeslotsByEnd.tailMap(query.getStart(), false).values();
        Set<Timeslot> endAfterIntervalStartSet = new HashSet<>(endAfterIntervalStart);
        var resultsStream = startBeforeIntervalEnd.stream()
                .filter(endAfterIntervalStartSet::contains);
        if (query.getTimeslotType() != null) {
            resultsStream = resultsStream.filter(
                    timeslot -> timeslot.getTypeId().equals(query.getTimeslotType())
            );
        }
        return resultsStream.collect(Collectors.toList());
    }
}
