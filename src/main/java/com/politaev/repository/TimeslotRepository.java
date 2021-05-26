package com.politaev.repository;

import com.politaev.model.Query;
import com.politaev.model.Timeslot;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface TimeslotRepository {
    Map<UUID, List<Timeslot>> findTimeslotsForUsers(Query query);
}
