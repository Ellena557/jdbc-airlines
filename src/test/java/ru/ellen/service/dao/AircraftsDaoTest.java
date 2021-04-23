package ru.ellen.service.dao;


import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.ellen.domain.Aircrafts;
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

import static ru.ellen.service.dao.TestData.FIRSTAIRCRAFT;
import static ru.ellen.service.dao.TestData.SECONDAIRCRAFT;

public class AircraftsDaoTest {
    private static DataSource s = JdbcConnectionPool.create("jdbc:h2:mem:database;DB_CLOSE_DELAY=-1",
            "", "");
    private static SimpleJdbcTemplate source = new SimpleJdbcTemplate(
            s);
    private static DbWorker db = new DbWorker(source);
    private AircraftsDao aircraftsDao = new AircraftsDao(source);

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

    private int getAircraftsCount() throws SQLException {
        return source.statement(stmt -> {
            ResultSet resultSet = stmt.executeQuery("select count(*) from aircrafts");
            resultSet.next();
            return resultSet.getInt(1);
        });
    }

    private Collection<Aircrafts> getTestAircrafts() {
        return Arrays.asList(FIRSTAIRCRAFT, SECONDAIRCRAFT);
    }

    @Test
    public void saveAircrafts() throws SQLException {
        Collection<Aircrafts> testAircrafts = getTestAircrafts();
        Assert.assertEquals(0, getAircraftsCount());
        aircraftsDao.saveAircrafts(testAircrafts);
        Assert.assertEquals(testAircrafts.size(), getAircraftsCount());
    }

    @Test
    public void getAIrcrafts() throws SQLException {
        Collection<Aircrafts> testAircrafts = getTestAircrafts();
        aircraftsDao.saveAircrafts(testAircrafts);
        Set<Aircrafts> aircrafts = aircraftsDao.getAircrafts();
        Assert.assertNotSame(testAircrafts, aircrafts);
        Assert.assertEquals(new HashSet<>(testAircrafts), aircrafts);
    }
}
