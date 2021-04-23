package ru.ellen.domain;

import lombok.Data;

@Data
public class Seats {
    private final String aircraftCode;
    private final String seatNo;
    private final String fareConditions;
}
