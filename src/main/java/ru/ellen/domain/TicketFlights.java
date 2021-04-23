package ru.ellen.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TicketFlights {
    private final String ticketNo;
    private final int flightId;
    private final String fareConditions;
    private final BigDecimal amount;
}
