package com.politaev.repository;

import java.util.UUID;

public interface CalendarRepository {
    boolean doCalendarsExist(UUID[] calendarIds);
}
