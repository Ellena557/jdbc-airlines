package ru.ellen.report;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;

@Data
public class ReportFormat7 extends ReportFormat {
    private final String day;
    private final long loss;

    public static ArrayList<String> headers
            = new ArrayList<>(Arrays.asList("Day", "Money loss"));

    public ArrayList<Object> getItems() {
        return new ArrayList<>(Arrays.asList(day, loss));
    }
}
