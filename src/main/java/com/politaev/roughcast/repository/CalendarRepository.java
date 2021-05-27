package com.politaev.roughcast.repository;

import java.util.UUID;

public interface CalendarRepository {
    boolean doCalendarsExist(UUID[] calendarIds);
}
