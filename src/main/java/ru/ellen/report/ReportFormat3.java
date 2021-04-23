package ru.ellen.report;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;

@Data
public class ReportFormat3 extends ReportFormat {
    private final String departure;
    private final String arrival;
    private final Long avgTime;

    public static ArrayList<String> headers
            = new ArrayList<>(Arrays.asList("Departure city", "Arrival city", "Average flight duration"));

    public ArrayList<Object> getItems() {
        return new ArrayList<>(Arrays.asList(departure, arrival, avgTime));
    }
}
