package com.politaev.model;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.politaev.model.Query.buildQuery;
import static java.util.UUID.randomUUID;

public class QueryTest {

    @Test
    public void testBuildQuery() {
        var calendarIds = new UUID[]{randomUUID(), randomUUID()};
        int duration = 12;
        var start = LocalDateTime.now();
        var end = start.plusHours(2);

        Query query = buildQuery()
                .withCalendarIds(calendarIds)
                .withDurationMinutes(duration)
                .withStart(start)
                .withEnd(end);

        Assert.assertEquals(calendarIds, query.getCalendarIds());
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

        Assert.assertEquals(calendarIds, query.getCalendarIds());
        Assert.assertEquals(duration, query.getDuration());
        Assert.assertEquals(start, query.getStart());
        Assert.assertEquals(end, query.getEnd());
        Assert.assertEquals(timeslotType, query.getTimeslotType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildQueryStartAfterEnd() {
        var calendarIds = new UUID[]{randomUUID(), randomUUID()};
        int duration = 12;
        var start = LocalDateTime.now();
        var end = start.minusHours(2);

        buildQuery()
                .withCalendarIds(calendarIds)
                .withDurationMinutes(duration)
                .withStart(start)
                .withEnd(end);
    }
}