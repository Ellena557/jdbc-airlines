package ru.ellen.service.dao;

import lombok.AllArgsConstructor;
import ru.ellen.domain.Seats;
import ru.ellen.service.db.SimpleJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class SeatsDao {
    private final SimpleJdbcTemplate source;

    private Seats createSeats(ResultSet resultSet) throws SQLException {
        return new Seats(resultSet.getString("aircraft_code"),
                resultSet.getString("seat_no"),
                resultSet.getString("fare_conditions"));
    }

    public void saveSeats(Collection<Seats> seats) throws SQLException {
        source.preparedStatement("insert into seats(aircraft_code, " +
                "seat_no, fare_conditions) values (?, ?, ?)", insertSeat -> {
            for (Seats seat : seats) {
                insertSeat.setString(1, seat.getAircraftCode());
                insertSeat.setObject(2, seat.getSeatNo());
                insertSeat.setString(3, seat.getFareConditions());
                insertSeat.execute();
            }
        });
    }

    public Set<Seats> getSeats() throws SQLException {
        return source.statement(stmt -> {
            Set<Seats> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("select * from seats");
            while (resultSet.next()) {
                result.add(createSeats(resultSet));
            }
            return result;
        });
    }
}
