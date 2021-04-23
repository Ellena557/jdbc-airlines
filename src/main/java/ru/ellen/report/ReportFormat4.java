package ru.ellen.report;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;

@Data
public class ReportFormat4 extends ReportFormat {
    private final String month;
    private final int numCancelled;

    public static ArrayList<String> headers
            = new ArrayList<>(Arrays.asList("Month", "Number of cancelled"));

    public ArrayList<Object> getItems() {
        return new ArrayList<>(Arrays.asList(month, numCancelled));
    }
}
