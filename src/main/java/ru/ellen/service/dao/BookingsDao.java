package ru.ellen.service.dao;

import lombok.AllArgsConstructor;
import ru.ellen.domain.Bookings;
import ru.ellen.service.db.SimpleJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class BookingsDao {
    private final SimpleJdbcTemplate source;

    private Bookings createBookings(ResultSet resultSet) throws SQLException {
        return new Bookings(resultSet.getString("book_ref"),
                resultSet.getTimestamp("book_date"),
                resultSet.getBigDecimal("total_amount"));
    }

    public void saveBookings(Collection<Bookings> bookings) throws SQLException {
        source.preparedStatement("insert into bookings(book_ref, " +
                "book_date, total_amount) values (?, ?, ?)", insertBooking -> {
            for (Bookings booking : bookings) {
                insertBooking.setString(1, booking.getBookRef());
                insertBooking.setTimestamp(2, booking.getBookDate());
                insertBooking.setBigDecimal(3, booking.getTotalAmount());
                insertBooking.execute();
            }
        });
    }

    public Set<Bookings> getBookings() throws SQLException {
        return source.statement(stmt -> {
            Set<Bookings> result = new HashSet<>();
            ResultSet resultSet = stmt.executeQuery("select * from bookings");
            while (resultSet.next()) {
                result.add(createBookings(resultSet));
            }
            return result;
        });
    }
}
