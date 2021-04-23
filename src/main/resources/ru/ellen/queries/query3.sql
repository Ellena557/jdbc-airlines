WITH shortest_paths AS
(
  SELECT DISTINCT airports.city, min(flights.scheduled_arrival - flights.scheduled_departure) as shortest
  FROM flights
  INNER JOIN airports
  ON flights.departure_airport = airports.airport_code
  WHERE flights.status = 'Arrived'
  GROUP BY airports.city
  ORDER BY shortest
)
SELECT DISTINCT dep_airports.city as departure, arr_airports.city as arrival,
flights.scheduled_arrival - flights.scheduled_departure as scheduled,
AVG(DATEDIFF(ss, flights.scheduled_departure, flights.scheduled_arrival)) as avg_time
FROM flights
INNER JOIN airports as dep_airports
ON flights.departure_airport = dep_airports.airport_code
INNER JOIN airports as arr_airports
ON flights.arrival_airport = arr_airports.airport_code
WHERE (dep_airports.city, flights.scheduled_arrival - flights.scheduled_departure)
IN (SELECT * FROM shortest_paths)
GROUP BY dep_airports.city, arr_airports.city, flights.scheduled_arrival - flights.scheduled_departure
ORDER BY avg_time ASC;