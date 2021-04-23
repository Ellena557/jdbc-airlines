package ru.ellen.domain;

import lombok.Data;

@Data
public class BoardingPasses {
    private final String ticketNo;
    private final int flightId;
    private final int boardingNo;
    private final String seatNo;
}
