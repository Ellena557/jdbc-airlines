package ru.ellen.service.dao;

import lombok.AllArgsConstructor;
import ru.ellen.domain.Tickets;
import ru.ellen.service.db.SimpleJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class TicketsDao {
    private final SimpleJdbcTemplate source;

    private Tickets createTickets(ResultSet resultSet) throws SQLException {
        return new Tickets(resultSet.getString("ticket_no"),
                resultSet.getString("book_ref"),
                resultSet.getString("passenger_id"),
                resultSet.getString("passenger_name"),
                resultSet.getString("contact_data"));
    }

    public void saveTickets(Collection<Tickets> tickets) throws SQLException {
        source.preparedStatement("insert into tickets(ticket_no, " +
                "book_ref, passenger_id, passenger_name, contact_data) values (?, ?, ?, ?, ?)", insertTicket -> {
            for (Tickets ticket : tickets) {
                insertTicket.setString(1, ticket.getTicketNo());
                insertTicket.setString(2, ticket.getBookRef());
                insertTicket.setString(3, ticket.getPassengerId());
                insertTicket.setString(4, ticket.getPassengerName());
                insertTicket.setString(5, ticket.getContactData());
                insertTicket.execute();
            }
        });
    }

    public Set<Tickets> getTickets() throws SQLException {
        return source.statement(stmt -> {
            Set<Tickets> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("select * from tickets");
            while (resultSet.next()) {
                result.add(createTickets(resultSet));
            }
            return result;
        });
    }
}
