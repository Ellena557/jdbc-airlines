package ru.ellen.service.dao;

import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.ellen.domain.TicketFlights;
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

public class TicketFlightsDaoTest {
    private static DataSource s = JdbcConnectionPool.create("jdbc:h2:mem:database;DB_CLOSE_DELAY=-1",
            "", "");
    private static SimpleJdbcTemplate source = new SimpleJdbcTemplate(
            s);
    private static DbWorker db = new DbWorker(source);
    private TicketFlightsDao ticketflightsDao = new TicketFlightsDao(source);

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

    private int getTicketFlightsCount() throws SQLException {
        return source.statement(stmt -> {
            ResultSet resultSet = stmt.executeQuery("select count(*) from ticket_flights");
            resultSet.next();
            return resultSet.getInt(1);
        });
    }

    private Collection<TicketFlights> getTestTicketFlights() {
        return Arrays.asList(FIRSTTICKETFLIGHT, SECONDTICKETFLIGHT);
    }

    @Test
    public void saveTicketFlights() throws SQLException {
        Collection<TicketFlights> testTicketFlights = getTestTicketFlights();
        Assert.assertEquals(0, getTicketFlightsCount());
        ticketflightsDao.saveTicketFlights(testTicketFlights);
        Assert.assertEquals(testTicketFlights.size(), getTicketFlightsCount());
    }

    @Test
    public void getTicketFlights() throws SQLException {
        Collection<TicketFlights> testTicketFlights = getTestTicketFlights();
        ticketflightsDao.saveTicketFlights(testTicketFlights);
        Set<TicketFlights> ticketflights = ticketflightsDao.getTicketFlights();
        Assert.assertNotSame(testTicketFlights, ticketflights);
        Assert.assertEquals(testTicketFlights.size(), ticketflights.size());
    }
}