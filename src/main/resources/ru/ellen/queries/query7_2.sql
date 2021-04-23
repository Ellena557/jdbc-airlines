WITH airports_msk AS
(
  SELECT airport_code
  FROM airports
  WHERE airports.city like '%Moscow%'
)
UPDATE flights
SET status = 'Cancelled'
WHERE (departure_airport IN (SELECT * FROM airports_msk) OR arrival_airport IN (SELECT * FROM airports_msk))
AND scheduled_departure >= ? AND scheduled_arrival <= ?
AND status != 'Cancelled' AND status != 'Arrived' AND status != 'Departed';