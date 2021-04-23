package ru.ellen.service.dao;

import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.ellen.domain.Bookings;
import ru.ellen.service.db.DbWorker;
import ru.ellen.service.db.SimpleJdbcTemplate;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import static ru.ellen.service.dao.TestData.*;

public class BookingsDaoTest {
    private static DataSource s = JdbcConnectionPool.create("jdbc:h2:mem:database;DB_CLOSE_DELAY=-1",
            "", "");
    private static SimpleJdbcTemplate source = new SimpleJdbcTemplate(
            s);
    private static DbWorker db = new DbWorker(source);
    private BookingsDao bookingsDao = new BookingsDao(source);

    @Before
    public void setupDB() throws IOException, SQLException {
        db.create();
    }

    @After
    public void tearDownDB() throws SQLException {
        source.statement(stmt -> {
            stmt.execute("drop all objects;");
        });
    }

    private int getBookingsCount() throws SQLException {
        return source.statement(stmt -> {
            ResultSet resultSet = stmt.executeQuery("select count(*) from bookings");
            resultSet.next();
            return resultSet.getInt(1);
        });
    }

    private Collection<Bookings> getTestbookings() {
        return Arrays.asList(FIRSTBOOKING, SECONDBOOKING, THIRDBOOKING);
    }

    @Test
    public void saveBookings() throws SQLException {
        Collection<Bookings> testBookings = getTestbookings();
        Assert.assertEquals(0, getBookingsCount());
        bookingsDao.saveBookings(testBookings);
        Assert.assertEquals(testBookings.size(), getBookingsCount());
    }

    @Test
    public void getBookings() throws SQLException {
        Collection<Bookings> testBookings = getTestbookings();
        bookingsDao.saveBookings(testBookings);
        Set<Bookings> bookings = bookingsDao.getBookings();
        Assert.assertNotSame(testBookings, bookings);
        Assert.assertEquals(testBookings.size(), bookings.size());
    }
}