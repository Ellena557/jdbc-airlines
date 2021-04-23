WITH msk_codes AS
(
  SELECT airport_code
  FROM airports
  WHERE airports.city like '%Moscow%'
),
cancelled_flights AS (
  SELECT flight_id as flight_id, scheduled_departure as scheduled_departure
  FROM flights
  WHERE (departure_airport IN (SELECT * FROM msk_codes) OR arrival_airport IN (SELECT * FROM msk_codes))
  AND scheduled_departure >= ? AND scheduled_arrival <= ?
  AND flights.status not like 'Cancelled' AND flights.status not like 'Arrived' AND flights.status not like 'Departed'
 )
SELECT to_char(scheduled_departure, 'yyyy-mm-dd') as day_cancelled, sum(ticket_flights.amount) as money_loss
FROM ticket_flights JOIN cancelled_flights
ON ticket_flights.flight_id = cancelled_flights.flight_id
GROUP BY day_cancelled
ORDER BY day_cancelled;