SELECT city as city, count(*) as cnt
FROM flights INNER JOIN airports
ON flights.departure_airport = airports.airport_code
WHERE flights.status = 'Cancelled'
GROUP BY city
ORDER BY cnt DESC
LIMIT 15;