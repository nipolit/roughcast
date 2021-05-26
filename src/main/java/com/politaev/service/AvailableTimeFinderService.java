package com.politaev.service;

import com.politaev.model.Query;
import com.politaev.model.TimeInterval;

import java.util.List;

public interface AvailableTimeFinderService {
    List<TimeInterval> findAvailableTime(Query query);
}
