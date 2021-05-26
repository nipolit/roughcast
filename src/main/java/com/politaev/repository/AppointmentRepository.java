package com.politaev.repository;

import com.politaev.model.Appointment;
import com.politaev.model.Query;

import java.util.List;

public interface AppointmentRepository {
    List<Appointment> findAppointments(Query query);
}
