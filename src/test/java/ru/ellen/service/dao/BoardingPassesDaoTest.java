package ru.ellen.service.dao;


import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.ellen.domain.BoardingPasses;
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

public class BoardingPassesDaoTest {
    private static DataSource s = JdbcConnectionPool.create("jdbc:h2:mem:database;DB_CLOSE_DELAY=-1",
            "", "");
    private static SimpleJdbcTemplate source = new SimpleJdbcTemplate(
            s);
    private static DbWorker db = new DbWorker(source);
    private BoardingPassesDao boardingPassesDao = new BoardingPassesDao(source);

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

    private int getBoardingPassesCount() throws SQLException {
        return source.statement(stmt -> {
            ResultSet resultSet = stmt.executeQuery("select count(*) from boarding_passes");
            resultSet.next();
            return resultSet.getInt(1);
        });
    }

    private Collection<BoardingPasses> getTestBoardingPasses() {
        return Arrays.asList(FIRSTPASSES, SECONDPASSES, THIRDPASSES);
    }

    @Test
    public void saveBoardingPasses() throws SQLException {
        Collection<BoardingPasses> testBoardingPasses = getTestBoardingPasses();
        Assert.assertEquals(0, getBoardingPassesCount());
        boardingPassesDao.saveBoardingPasses(testBoardingPasses);
        Assert.assertEquals(testBoardingPasses.size(), getBoardingPassesCount());
    }

    @Test
    public void getBoardingPasses() throws SQLException {
        Collection<BoardingPasses> testBoardingPasses = getTestBoardingPasses();
        boardingPassesDao.saveBoardingPasses(testBoardingPasses);
        Set<BoardingPasses> boardingPasses = boardingPassesDao.getBoardingPasses();
        Assert.assertNotSame(testBoardingPasses, boardingPasses);
        Assert.assertEquals(new HashSet<>(testBoardingPasses), boardingPasses);
    }
}
