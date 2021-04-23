package ru.ellen.service.dao;

import lombok.AllArgsConstructor;
import ru.ellen.domain.TicketFlights;
import ru.ellen.service.db.SimpleJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class TicketFlightsDao {
    private final SimpleJdbcTemplate source;

    private TicketFlights createTicketFlights(ResultSet resultSet) throws SQLException {
        return new TicketFlights(resultSet.getString("ticket_no"),
                resultSet.getInt("flight_id"),
                resultSet.getString("fare_conditions"),
                resultSet.getBigDecimal("amount"));
    }

    public void saveTicketFlights(Collection<TicketFlights> ticketFlights) throws SQLException {
        source.preparedStatement("insert into ticket_flights(ticket_no, " +
                "flight_id, fare_conditions, amount) values (?, ?, ?, ?)", insertTicketFlights -> {
            for (TicketFlights ticketFlight : ticketFlights) {
                insertTicketFlights.setString(1, ticketFlight.getTicketNo());
                insertTicketFlights.setInt(2, ticketFlight.getFlightId());
                insertTicketFlights.setString(3, ticketFlight.getFareConditions());
                insertTicketFlights.setBigDecimal(4, ticketFlight.getAmount());
                insertTicketFlights.execute();
            }
        });
    }

    public Set<TicketFlights> getTicketFlights() throws SQLException {
        return source.statement(stmt -> {
            Set<TicketFlights> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("select * from ticket_flights");
            while (resultSet.next()) {
                result.add(createTicketFlights(resultSet));
            }
            return result;
        });
    }
}
