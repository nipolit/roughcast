package com.politaev.repository;

import com.politaev.model.Query;
import com.politaev.model.Timeslot;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableNavigableMap;

public class TimeslotRepositoryImpl implements TimeslotRepository {
    private final NavigableMap<LocalDateTime, Timeslot> timeslotsByStart;
    private final NavigableMap<LocalDateTime, Timeslot> timeslotsByEnd;

    public TimeslotRepositoryImpl(List<StoredData> storedDataList) {
        NavigableMap<LocalDateTime, Timeslot> timeslotsByStart = new TreeMap<>();
        NavigableMap<LocalDateTime, Timeslot> timeslotsByEnd = new TreeMap<>();
        for (StoredData storedData : storedDataList) {
            for (Timeslot timeslot : storedData.getTimeslots()) {
                timeslotsByStart.put(timeslot.getStart(), timeslot);
                timeslotsByEnd.put(timeslot.getEnd(), timeslot);
            }
        }
        this.timeslotsByStart = unmodifiableNavigableMap(timeslotsByStart);
        this.timeslotsByEnd = unmodifiableNavigableMap(timeslotsByEnd);
    }

    @Override
    public List<Timeslot> findTimeslots(Query query) {
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
