package com.politaev;

import com.politaev.model.Query;
import com.politaev.model.TimeInterval;
import picocli.CommandLine;

import java.util.List;
import java.util.UUID;

import static com.politaev.controller.ApplicationController.initializeApplicationContextAndCreateController;

@CommandLine.Command(
        name = "roughcast",
        description = "Searches for time intervals that are available in multiple calendars",
        version = {"roughcast 1.0", "@|green Nikita Politaev (c) 2021|@"},
        sortOptions = false,
        mixinStandardHelpOptions = true,
        headerHeading = "@|bold,underline Usage|@:%n%n",
        descriptionHeading = "%n@|bold,underline Description|@:%n%n",
        parameterListHeading = "%n@|bold,underline Parameters|@:%n",
        optionListHeading = "%n@|bold,underline Options|@:%n",
        footerHeading = "%n",
        footer = {"@|green Nikita Politaev (c) 2021|@"},
        customSynopsis = "roughcast [-hV] -d=DURATION -i=INTERVAL -t=TYPE CALENDARS...")
public class App implements Runnable {

    @CommandLine.Option(
            names = {"-d", "--duration"},
            required = true,
            description = "Target interval duration",
            paramLabel = "DURATION")
    private int duration;

    @CommandLine.Option(
            names = {"-i", "--interval"},
            required = true,
            description = "Lookup time interval",
            paramLabel = "INTERVAL")
    private TimeInterval lookupInterval;

    @CommandLine.Option(
            names = {"-t", "--type"},
            description = "Target interval availability type",
            paramLabel = "TYPE")
    private UUID timeslotType;

    @CommandLine.Parameters(
            arity = "1..*",
            description = "Calendars that all must be available.",
            paramLabel = "CALENDARS"
    )
    private UUID[] calendarIds;

    public static void main(String[] args) {
        new CommandLine(new App())
                .registerConverter(UUID.class, UUID::fromString)
                .registerConverter(TimeInterval.class, TimeInterval::parse)
                .execute(args);
    }

    @Override
    public void run() {
        try {
            var applicationController = initializeApplicationContextAndCreateController();
            var query = Query.buildQuery()
                    .withCalendarIds(calendarIds)
                    .withDurationMinutes(duration)
                    .withStart(lookupInterval.getStart())
                    .withEnd(lookupInterval.getEnd());
            if (timeslotType != null) {
                query = query.withTimeslotType(timeslotType);
            }
            List<TimeInterval> availableTimeIntervals = applicationController.findAvailableTime(query);
            printResult(availableTimeIntervals);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    private void printResult(List<TimeInterval> availableTimeIntervals) {
        if (availableTimeIntervals.isEmpty()) {
            System.out.println("Found no available time intervals.");
            return;
        }
        System.out.println("Available time intervals:");
        for (var timeInterval : availableTimeIntervals) {
            System.out.println(timeInterval);
        }
    }
}
