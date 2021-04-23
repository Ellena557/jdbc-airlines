package ru.ellen.domain;

import lombok.Data;

@Data
public class Aircrafts {
    private final String aircraftCode;
    private final String model;
    private final int range;
}
