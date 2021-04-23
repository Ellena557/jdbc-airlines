package ru.ellen.service.dao;

import lombok.AllArgsConstructor;
import ru.ellen.domain.Aircrafts;
import ru.ellen.service.db.SimpleJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class AircraftsDao {
    private final SimpleJdbcTemplate source;

    private Aircrafts createAircrafts(ResultSet resultSet) throws SQLException {
        return new Aircrafts(resultSet.getString("aircraft_code"),
                resultSet.getString("model"),
                resultSet.getInt("range"));
    }

    public void saveAircrafts(Collection<Aircrafts> aircrafts) throws SQLException {
        source.preparedStatement("insert into aircrafts(aircraft_code, model, range) values (?, ?, ?)", insertAircraft -> {
            for (Aircrafts aircraft : aircrafts) {
                insertAircraft.setString(1, aircraft.getAircraftCode());
                insertAircraft.setString(2, aircraft.getModel());
                insertAircraft.setInt(3, aircraft.getRange());
                insertAircraft.execute();
            }
        });
    }

    public Set<Aircrafts> getAircrafts() throws SQLException {
        return source.statement(stmt -> {
            Set<Aircrafts> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("select * from aircrafts");
            while (resultSet.next()) {
                result.add(createAircrafts(resultSet));
            }
            return result;
        });
    }
}
