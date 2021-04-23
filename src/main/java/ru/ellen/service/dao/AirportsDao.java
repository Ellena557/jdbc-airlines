package ru.ellen.service.dao;

import lombok.AllArgsConstructor;
import ru.ellen.domain.Airports;
import ru.ellen.service.db.SimpleJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class AirportsDao {
    private final SimpleJdbcTemplate source;

    private Airports createAirports(ResultSet resultSet) throws SQLException {
        return new Airports(resultSet.getString("airport_code"),
                resultSet.getString("airport_name"),
                resultSet.getString("city"),
                resultSet.getString("coordinates"),
                resultSet.getString("timezone"));
    }

    public void saveAirports(Collection<Airports> airports) throws SQLException {
        source.preparedStatement("insert into airports(airport_code, " +
                "airport_name, city, coordinates, timezone) values (?, ?, ?, ?, ?)", insertAirport -> {
            for (Airports airport : airports) {
                insertAirport.setString(1, airport.getAirportCode());
                insertAirport.setString(2, airport.getAirportName().toString());
                insertAirport.setString(3, airport.getCity().toString());
                insertAirport.setString(4, airport.getCoordinates());
                insertAirport.setString(5, airport.getTimezone());
                insertAirport.execute();
            }
        });
    }

    public Set<Airports> getAirports() throws SQLException {
        return source.statement(stmt -> {
            Set<Airports> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("select * from airports");
            while (resultSet.next()) {
                result.add(createAirports(resultSet));
            }
            return result;
        });
    }
}
