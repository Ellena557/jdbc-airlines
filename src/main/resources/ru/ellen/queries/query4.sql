SELECT MONTH(scheduled_departure) as mon, count(*) as num_cancelled
FROM flights
WHERE status = 'Cancelled'
GROUP BY mon;