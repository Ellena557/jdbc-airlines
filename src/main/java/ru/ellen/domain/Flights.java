package ru.ellen.domain;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Flights {
    private final long flightId;
    private final String flightNo;
    private final Timestamp scheduledDeparture;
    private final Timestamp scheduledArrival;
    private final String departureAirport;
    private final String arrivalAirport;
    private final String status;
    private final String aircraftCode;
    private final Timestamp actualDeparture;
    private final Timestamp actualArrival;
}
