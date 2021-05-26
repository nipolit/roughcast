package com.politaev.repository;

import com.politaev.model.Timeslot;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class CalendarRepositoryImpl implements CalendarRepository {
    private final Set<UUID> knownCalendarIds;

    public CalendarRepositoryImpl(List<StoredData> storedDataList) {
        var timeslotsStream = storedDataList.stream()
                .map(StoredData::getTimeslots)
                .flatMap(List::stream);
        knownCalendarIds = timeslotsStream
                .map(Timeslot::getCalendarId)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean doCalendarsExist(UUID[] calendarIds) {
        return knownCalendarIds.containsAll(Arrays.asList(calendarIds));
    }
}
