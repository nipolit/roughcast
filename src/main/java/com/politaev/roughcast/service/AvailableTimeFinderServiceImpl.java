package com.politaev.roughcast.service;

import com.politaev.roughcast.model.*;
import com.politaev.roughcast.repository.AppointmentRepository;
import com.politaev.roughcast.repository.TimeslotRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AvailableTimeFinderServiceImpl implements AvailableTimeFinderService {
    private final TimeslotRepository timeslotRepository;
    private final AppointmentRepository appointmentRepository;

    public AvailableTimeFinderServiceImpl(TimeslotRepository timeslotRepository, AppointmentRepository appointmentRepository) {
        this.timeslotRepository = timeslotRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public List<TimeInterval> findAvailableTime(Query query) {
        var timeslots = timeslotRepository.findTimeslots(query);
        var appointments = appointmentRepository.findAppointments(query);
        var events = convertToEvents(timeslots, appointments);
        var availabilityIntervals = analyzeEvents(events, query.getCalendarIds());
        return availabilityIntervals.stream()
                .filter(timeInterval -> timeInterval.notShorter(query.getDuration()))
                .collect(Collectors.toList());
    }

    private List<Event> convertToEvents(List<Timeslot> timeslots, List<Appointment> appointments) {
        var eventsStream = Stream.concat(
                timeslots.stream().flatMap(Event::fromTimeslot),
                appointments.stream().flatMap(Event::fromAppointment)
        );
        var eventComparator = Comparator.comparing(Event::getDateTime).thenComparing(Event::getType);
        return eventsStream.sorted(eventComparator)
                .collect(Collectors.toList());
    }

    private List<TimeInterval> analyzeEvents(List<Event> events, UUID[] calendarIds) {
        var availabilityIntervals = new ArrayList<TimeInterval>();
        var activeTimeslots = counterMap(calendarIds);
        var activeAppointments = counterMap(calendarIds);
        LocalDateTime activeAvailabilityIntervalStart = null;
        for (var event : events) {
            switch (event.getType()) {
                case TIMESLOT_START:
                    incrementCounter(activeTimeslots, event.getCalendarId());
                    break;
                case TIMESLOT_END:
                    decrementCounter(activeTimeslots, event.getCalendarId());
                    break;
                case APPOINTMENT_START:
                    incrementCounter(activeAppointments, event.getCalendarId());
                    break;
                case APPOINTMENT_END:
                    decrementCounter(activeAppointments, event.getCalendarId());
                    break;
            }
            if (activeAvailabilityIntervalStart == null) {
                if (allZeroes(activeAppointments) && allPositive(activeTimeslots)) {
                    activeAvailabilityIntervalStart = event.getDateTime();
                }
            } else {
                if (hasPositive(activeAppointments) || hasZero(activeTimeslots)) {
                    var timeInterval = new TimeInterval(activeAvailabilityIntervalStart, event.getDateTime());
                    activeAvailabilityIntervalStart = null;
                    availabilityIntervals.add(timeInterval);
                }
            }
        }
        return availabilityIntervals.stream()
                .filter(Predicate.not(TimeInterval::isZeroLength))
                .collect(Collectors.toList());
    }

    private Map<UUID, Integer> counterMap(UUID[] calendarIds) {
        return Stream.of(calendarIds)
                .collect(Collectors.toMap(Function.identity(), k -> 0));
    }

    private void incrementCounter(Map<UUID, Integer> counterMap, UUID calendarId) {
        var counterValue = counterMap.get(calendarId);
        counterValue++;
        counterMap.put(calendarId, counterValue);
    }

    private void decrementCounter(Map<UUID, Integer> counterMap, UUID calendarId) {
        var counterValue = counterMap.get(calendarId);
        counterValue--;
        counterMap.put(calendarId, counterValue);
    }

    private boolean allZeroes(Map<UUID, Integer> counterMap) {
        return counterMap.values().stream().allMatch(i -> i == 0);
    }

    private boolean allPositive(Map<UUID, Integer> counterMap) {
        return counterMap.values().stream().allMatch(i -> i > 0);
    }

    private boolean hasPositive(Map<UUID, Integer> counterMap) {
        return counterMap.values().stream().anyMatch(i -> i > 0);
    }

    private boolean hasZero(Map<UUID, Integer> counterMap) {
        return counterMap.values().stream().anyMatch(i -> i == 0);
    }
}
