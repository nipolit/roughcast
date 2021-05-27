package com.politaev.roughcast.repository;

import com.politaev.roughcast.model.Query;
import com.politaev.roughcast.model.Timeslot;

import java.util.List;

public interface TimeslotRepository {
    List<Timeslot> findTimeslots(Query query);
}
