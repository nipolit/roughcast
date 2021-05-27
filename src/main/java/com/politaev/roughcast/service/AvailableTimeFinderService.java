package com.politaev.roughcast.service;

import com.politaev.roughcast.model.Query;
import com.politaev.roughcast.model.TimeInterval;

import java.util.List;

public interface AvailableTimeFinderService {
    List<TimeInterval> findAvailableTime(Query query);
}
