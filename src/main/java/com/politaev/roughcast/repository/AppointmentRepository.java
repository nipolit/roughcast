package com.politaev.roughcast.repository;

import com.politaev.roughcast.model.Appointment;
import com.politaev.roughcast.model.Query;

import java.util.List;

public interface AppointmentRepository {
    List<Appointment> findAppointments(Query query);
}
