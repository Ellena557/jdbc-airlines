DELETE FROM flights
WHERE flights.aircraft_code like ?;
DELETE FROM ticket_flights
WHERE ticket_flights.flight_id not in (select flight_id from flights);
DELETE FROM tickets
WHERE tickets.ticket_no not in (select ticket_no from ticket_flights);