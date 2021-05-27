package com.politaev.repository;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class DataReaderTest {

    @Test
    public void testReadStoredData() {
        DataReader dataReader = new DataReader();
        List<StoredData> result = dataReader.readStoredData("data_reader/calendar_data.json");
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getAppointments().size());
        assertEquals(1, result.get(0).getTimeslots().size());
    }

    @Test
    public void testReadStoredDataExtraFields() {
        DataReader dataReader = new DataReader();
        List<StoredData> result = dataReader.readStoredData("data_reader/calendar_data_extra_fields.json");
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getAppointments().size());
        assertEquals(1, result.get(0).getTimeslots().size());
    }

    @Test
    public void testReadStoredDataMultipleEntries() {
        DataReader dataReader = new DataReader();
        List<StoredData> result = dataReader.readStoredData("data_reader/calendar_data_multiple_entries.json");
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getAppointments().size());
        assertEquals(1, result.get(0).getTimeslots().size());
        assertEquals(1, result.get(1).getAppointments().size());
        assertEquals(1, result.get(1).getTimeslots().size());
    }
}