package ru.ellen.service.dao;

import ru.ellen.domain.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class TestData {
    public static Aircrafts FIRSTAIRCRAFT = new Aircrafts("123",
            "{\"en\": \"Boeing-777\", \"ru\": \"Боинг-777\"}",
            12);
    public static Aircrafts SECONDAIRCRAFT = new Aircrafts("456",
            "{\"en\": \"Boeing-666\", \"ru\": \"Боинг-666\"}",
            5);
    public static Airports FIRSTAIRPORT = new Airports(
            "1", "Hogwarts", "London", "12, 12", "Europe");
    public static Airports SECONDAIRPORT = new Airports(
            "2", "AirName", "Moscow", "55, 55", "Europe");
    public static BoardingPasses FIRSTPASSES =
            new BoardingPasses("1234", 12, 15, "3C");
    public static BoardingPasses SECONDPASSES =
            new BoardingPasses("5678", 15, 12, "5B");
    public static BoardingPasses THIRDPASSES =
            new BoardingPasses("9012", 18, 10, "7A");
    public static Bookings FIRSTBOOKING = new Bookings(
            "12", new Timestamp(Date.valueOf("2017-01-01").getTime()), new BigDecimal(125.00));
    public static Bookings SECONDBOOKING = new Bookings(
            "17", new Timestamp(Date.valueOf("2018-06-01").getTime()), new BigDecimal(555.00));
    public static Bookings THIRDBOOKING = new Bookings(
            "19", new Timestamp(Date.valueOf("2019-01-05").getTime()), new BigDecimal(763.00));
    public static Flights FIRSTFLIGHT = new Flights(
            12, "11", new Timestamp(Date.valueOf("2019-01-05").getTime()),
            new Timestamp(Date.valueOf("2019-01-05").getTime()), "A", "B",
            "Arrived", "c11",
            new Timestamp(Date.valueOf("2019-01-05").getTime()),
            new Timestamp(Date.valueOf("2019-01-05").getTime()));
    public static Flights SECONDFLIGHT = new Flights(
            14, "15", new Timestamp(Date.valueOf("2017-01-05").getTime()),
            new Timestamp(Date.valueOf("2017-01-05").getTime()), "B", "A",
            "Arrived", "c12",
            new Timestamp(Date.valueOf("2017-01-05").getTime()),
            new Timestamp(Date.valueOf("2017-01-05").getTime()));
    public static Seats FIRSTSEAT = new Seats("c11", "3A", "Comfort");
    public static Seats SECONDSEAT = new Seats("c12", "5C", "Comfort");
    public static TicketFlights FIRSTTICKETFLIGHT = new TicketFlights(
            "122", 12, "Comfort", new BigDecimal(666));
    public static TicketFlights SECONDTICKETFLIGHT = new TicketFlights(
            "333", 55, "Comfort", new BigDecimal(7777));
    public static Tickets FIRSTTICKET =
            new Tickets("1234", "123", "16",
                    "Harry Potter",
                    "{\"phone\": \"+777\", \"email\": \"harry@harry@harry\"}");
    public static Tickets SECONDTICKET =
            new Tickets("24", "24", "24",
                    "Jack Bauer",
                    "{\"phone\": \"+24\", \"email\": \"jack@bauer\"}");
}
