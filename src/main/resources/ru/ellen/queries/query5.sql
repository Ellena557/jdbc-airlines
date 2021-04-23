WITH msk_codes AS
(
  SELECT DISTINCT airport_code
  FROM airports
  WHERE airports.city like '%Moscow%'
)
SELECT to_msk.arr_day, to_msk.incoming, from_msk.outcoming
FROM
(
  SELECT ISO_DAY_OF_WEEK(scheduled_arrival) AS arr_day, count(*) AS incoming
  FROM flights
  WHERE arrival_airport IN (SELECT * FROM msk_codes)
  GROUP BY arr_day
) AS to_msk
INNER JOIN
(
  SELECT ISO_DAY_OF_WEEK(scheduled_departure) AS dep_day, count(*) AS outcoming
  FROM flights
  WHERE departure_airport IN (SELECT * FROM msk_codes)
  GROUP BY dep_day
) AS from_msk
ON to_msk.arr_day = from_msk.dep_day