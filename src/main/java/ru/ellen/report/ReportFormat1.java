package ru.ellen.report;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;

@Data
public class ReportFormat1 extends ReportFormat {
    private final String city;
    private final String airportsList;

    public static ArrayList<String> headers
            = new ArrayList<>(Arrays.asList("City", "Airports' list"));

    public ArrayList<Object> getItems() {
        return new ArrayList<>(Arrays.asList(city, airportsList));
    }
}
