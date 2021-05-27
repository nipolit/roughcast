package com.politaev.roughcast.repository;

import com.politaev.roughcast.model.Appointment;
import com.politaev.roughcast.model.Timeslot;

import java.util.List;

public class StoredData {
    List<Appointment> appointments;
    List<Timeslot> timeslots;

    public StoredData() {
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public List<Timeslot> getTimeslots() {
        return timeslots;
    }

    public void setTimeslots(List<Timeslot> timeslots) {
        this.timeslots = timeslots;
    }
}
