package ru.ellen.report;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;

@Data
public class ReportFormat5 extends ReportFormat {
    private final String day;
    private final int incoming;
    private final int outcoming;

    public static ArrayList<String> headers
            = new ArrayList<>(Arrays.asList("Day", "Flights to Moscow", "Flights from Moscow"));

    public ArrayList<Object> getItems() {
        return new ArrayList<>(Arrays.asList(day, incoming, outcoming));
    }
}
