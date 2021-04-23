SELECT count(*)
FROM ticket_flights
WHERE flight_id = ? and fare_conditions = ?;