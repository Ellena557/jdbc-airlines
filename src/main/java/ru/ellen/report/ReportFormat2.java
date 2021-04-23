package ru.ellen.report;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;

@Data
public class ReportFormat2 extends ReportFormat {
    private final String city;
    private final String numCancelled;

    public static ArrayList<String> headers
            = new ArrayList<>(Arrays.asList("City", "Number of cancelled flights"));

    public ArrayList<Object> getItems() {
        return new ArrayList<>(Arrays.asList(city, numCancelled));
    }
}
