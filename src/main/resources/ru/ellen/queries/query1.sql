SELECT city, airportsList
FROM (
    SELECT city as city, STRING_AGG(airport_code, ',') as airportsList, count(*) as cnt
    FROM airports
    GROUP BY city
) as helper
WHERE cnt > 1;