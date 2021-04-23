package ru.ellen.domain;

import lombok.Data;

@Data
public class Airports {
    private final String airportCode;
    private final String airportName;
    private final String city;
    private final String coordinates;
    private final String timezone;
}
