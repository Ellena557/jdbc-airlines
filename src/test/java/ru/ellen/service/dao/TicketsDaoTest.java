package ru.ellen.service.dao;

import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.ellen.domain.Tickets;
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

public class TicketsDaoTest {
    private static DataSource s = JdbcConnectionPool.create("jdbc:h2:mem:database;DB_CLOSE_DELAY=-1",
            "", "");
    private static SimpleJdbcTemplate source = new SimpleJdbcTemplate(
            s);
    private static DbWorker db = new DbWorker(source);
    private TicketsDao ticketsDao = new TicketsDao(source);

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

    private int getTicketsCount() throws SQLException {
        return source.statement(stmt -> {
            ResultSet resultSet = stmt.executeQuery("select count(*) from tickets");
            resultSet.next();
            return resultSet.getInt(1);
        });
    }

    private Collection<Tickets> getTestTickets() {
        return Arrays.asList(FIRSTTICKET, SECONDTICKET);
    }

    @Test
    public void saveTickets() throws SQLException {
        Collection<Tickets> testTickets = getTestTickets();
        Assert.assertEquals(0, getTicketsCount());
        ticketsDao.saveTickets(testTickets);
        Assert.assertEquals(testTickets.size(), getTicketsCount());
    }

    @Test
    public void getTickets() throws SQLException {
        Collection<Tickets> testTickets = getTestTickets();
        ticketsDao.saveTickets(testTickets);
        Set<Tickets> tickets = ticketsDao.getTickets();
        Assert.assertNotSame(testTickets, tickets);
        Assert.assertEquals(new HashSet<>(testTickets), tickets);
    }
}