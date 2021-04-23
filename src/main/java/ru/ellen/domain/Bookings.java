package ru.ellen.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class Bookings {
    private final String bookRef;
    private final Timestamp bookDate;
    private final BigDecimal totalAmount;
}
