package com.politaev.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.politaev.model.Query.buildQuery;
import static java.util.UUID.randomUUID;

public class QueryTest {

    UUID[] calendarIds;
    int duration;
    LocalDateTime start;
    LocalDateTime end;
    UUID timeslotType;

    @Before
    public void setUp() {
        calendarIds = new UUID[]{randomUUID(), randomUUID()};
        duration = 12;
        start = LocalDateTime.now();
        end = start.plusHours(2);
        timeslotType = randomUUID();
    }

    @Test
    public void testBuildQuery() {
        Query query = buildQuery()
                .withCalendarIds(calendarIds)
                .withDurationMinutes(duration)
                .withStart(start)
                .withEnd(end);

        Assert.assertArrayEquals(calendarIds, query.getCalendarIds());
        Assert.assertEquals(duration, query.getDuration());
        Assert.assertEquals(start, query.getStart());
        Assert.assertEquals(end, query.getEnd());
        Assert.assertNull(query.getTimeslotType());
    }

    @Test
    public void testBuildQueryWithTimeslotType() {
        var calendarIds = new UUID[]{randomUUID(), randomUUID()};
        int duration = 12;
        var start = LocalDateTime.now();
        var end = start.plusHours(2);
        var timeslotType = randomUUID();

        Query query = buildQuery()
                .withCalendarIds(calendarIds)
                .withDurationMinutes(duration)
                .withStart(start)
                .withEnd(end)
                .withTimeslotType(timeslotType);

        Assert.assertArrayEquals(calendarIds, query.getCalendarIds());
        Assert.assertEquals(duration, query.getDuration());
        Assert.assertEquals(start, query.getStart());
        Assert.assertEquals(end, query.getEnd());
        Assert.assertEquals(timeslotType, query.getTimeslotType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildQueryEmptyCalendarIds() {
        buildQuery()
                .withCalendarIds(new UUID[]{})
                .withDurationMinutes(0)
                .withStart(start)
                .withEnd(end);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildQueryZeroDuration() {
        buildQuery()
                .withCalendarIds(calendarIds)
                .withDurationMinutes(0)
                .withStart(start)
                .withEnd(end);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildQueryDurationLongerThanInterval() {
        buildQuery()
                .withCalendarIds(calendarIds)
                .withDurationMinutes(61)
                .withStart(start)
                .withEnd(start.plusHours(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildQueryStartAfterEnd() {
        buildQuery()
                .withCalendarIds(calendarIds)
                .withDurationMinutes(duration)
                .withStart(start)
                .withEnd(start.minusHours(2));
    }
}