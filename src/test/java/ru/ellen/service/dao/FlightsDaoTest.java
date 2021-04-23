package ru.ellen.service.dao;

import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.ellen.domain.Flights;
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

public class FlightsDaoTest {
    private static DataSource s = JdbcConnectionPool.create("jdbc:h2:mem:database;DB_CLOSE_DELAY=-1",
            "", "");
    private static SimpleJdbcTemplate source = new SimpleJdbcTemplate(
            s);
    private static DbWorker db = new DbWorker(source);
    private FlightsDao flightsDao = new FlightsDao(source);

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

    private int getFlightsCount() throws SQLException {
        return source.statement(stmt -> {
            ResultSet resultSet = stmt.executeQuery("select count(*) from flights");
            resultSet.next();
            return resultSet.getInt(1);
        });
    }

    private Collection<Flights> getTestFlights() {
        return Arrays.asList(FIRSTFLIGHT, SECONDFLIGHT);
    }

    @Test
    public void saveFlights() throws SQLException {
        Collection<Flights> testFlights = getTestFlights();
        Assert.assertEquals(0, getFlightsCount());
        flightsDao.saveFlights(testFlights);
        Assert.assertEquals(testFlights.size(), getFlightsCount());
    }

    @Test
    public void getFlights() throws SQLException {
        Collection<Flights> testFlights = getTestFlights();
        flightsDao.saveFlights(testFlights);
        Set<Flights> flights = flightsDao.getFlights();
        Assert.assertNotSame(testFlights, flights);
        Assert.assertEquals(new HashSet<>(testFlights), flights);
    }
}