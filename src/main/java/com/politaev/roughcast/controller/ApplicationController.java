package com.politaev.roughcast.controller;

import com.politaev.roughcast.model.Query;
import com.politaev.roughcast.model.TimeInterval;
import com.politaev.roughcast.repository.*;
import com.politaev.roughcast.service.AvailableTimeFinderService;
import com.politaev.roughcast.service.AvailableTimeFinderServiceImpl;

import java.util.List;

public class ApplicationController {

    private final AvailableTimeFinderService availableTimeFinderService;
    private final CalendarRepository calendarRepository;

    public static ApplicationController initializeApplicationContextAndCreateController() {
        var dataReader = new DataReader();
        var calendarData = dataReader.readStoredData("calendar_data.json");
        var calendarRepository = new CalendarRepositoryImpl(calendarData);
        var appointmentRepository = new AppointmentRepositoryImpl(calendarData);
        var timeslotRepository = new TimeslotRepositoryImpl(calendarData);
        var availableTimeFinderService = new AvailableTimeFinderServiceImpl(timeslotRepository, appointmentRepository);
        return new ApplicationController(availableTimeFinderService, calendarRepository);
    }

    private ApplicationController(AvailableTimeFinderService availableTimeFinderService, CalendarRepository calendarRepository) {
        this.availableTimeFinderService = availableTimeFinderService;
        this.calendarRepository = calendarRepository;
    }

    public List<TimeInterval> findAvailableTime(Query query) {
        requireKnownCalendarIds(query);
        return availableTimeFinderService.findAvailableTime(query);
    }

    private void requireKnownCalendarIds(Query query) {
        if (!calendarRepository.doCalendarsExist(query.getCalendarIds())) {
            throw new IllegalArgumentException("No data found for some calendarIds");
        }
    }
}
