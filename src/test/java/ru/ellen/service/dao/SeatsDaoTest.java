package ru.ellen.service.dao;

import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.ellen.domain.Seats;
import ru.ellen.service.db.DbWorker;
import ru.ellen.service.db.SimpleJdbcTemplate;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static ru.ellen.service.dao.TestData.*;

public class SeatsDaoTest {
    private static DataSource s = JdbcConnectionPool.create("jdbc:h2:mem:database;DB_CLOSE_DELAY=-1",
            "", "");
    private static SimpleJdbcTemplate source = new SimpleJdbcTemplate(
            s);
    private static DbWorker db = new DbWorker(source);
    private SeatsDao seatsDao = new SeatsDao(source);

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

    private int getSeatsCount() throws SQLException {
        return source.statement(stmt -> {
            ResultSet resultSet = stmt.executeQuery("select count(*) from seats");
            resultSet.next();
            return resultSet.getInt(1);
        });
    }

    private Collection<Seats> getTestSeats() {
        return Arrays.asList(FIRSTSEAT, SECONDSEAT);
    }

    @Test
    public void saveSeats() throws SQLException {
        Collection<Seats> testSeats = getTestSeats();
        Assert.assertEquals(0, getSeatsCount());
        seatsDao.saveSeats(testSeats);
        Assert.assertEquals(testSeats.size(), getSeatsCount());
    }

    @Test
    public void getSeats() throws SQLException {
        Collection<Seats> testSeats = getTestSeats();
        seatsDao.saveSeats(testSeats);
        Set<Seats> seats = seatsDao.getSeats();
        Assert.assertNotSame(testSeats, seats);
        Assert.assertEquals(new HashSet<>(testSeats), seats);
    }
}