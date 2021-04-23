package ru.ellen.service.dao;

import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.ellen.domain.Airports;
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

import static ru.ellen.service.dao.TestData.FIRSTAIRPORT;
import static ru.ellen.service.dao.TestData.SECONDAIRPORT;


public class AirportsDaoTest {
    private static DataSource s = JdbcConnectionPool.create("jdbc:h2:mem:database;DB_CLOSE_DELAY=-1",
            "", "");
    private static SimpleJdbcTemplate source = new SimpleJdbcTemplate(
            s);
    private static DbWorker db = new DbWorker(source);
    private AirportsDao airportsDao = new AirportsDao(source);

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

    private int getAirportsCount() throws SQLException {
        return source.statement(stmt -> {
            ResultSet resultSet = stmt.executeQuery("select count(*) from airports");
            resultSet.next();
            return resultSet.getInt(1);
        });
    }

    private Collection<Airports> getTestAirports() {
        return Arrays.asList(FIRSTAIRPORT, SECONDAIRPORT);
    }

    @Test
    public void saveAirports() throws SQLException {
        Collection<Airports> testAirports = getTestAirports();
        Assert.assertEquals(0, getAirportsCount());
        airportsDao.saveAirports(testAirports);
        Assert.assertEquals(testAirports.size(), getAirportsCount());
    }

    @Test
    public void getAirports() throws SQLException {
        Collection<Airports> testAirports = getTestAirports();
        airportsDao.saveAirports(testAirports);
        Set<Airports> airports = airportsDao.getAirports();
        Assert.assertNotSame(testAirports, airports);
        Assert.assertEquals(new HashSet<>(testAirports), airports);
    }
}
