package com.politaev.repository;

import com.politaev.model.Query;
import com.politaev.model.Timeslot;

import java.util.List;

public interface TimeslotRepository {
    List<Timeslot> findTimeslots(Query query);
}
