package ru.ellen.domain;

import lombok.Data;

@Data
public class Tickets {
    private final String ticketNo;
    private final String bookRef;
    private final String passengerId;
    private final String passengerName;
    private final String contactData;
}
