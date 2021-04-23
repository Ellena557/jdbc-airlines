package ru.ellen.service.db;

import lombok.AllArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import ru.ellen.MainApp;
import ru.ellen.domain.*;
import ru.ellen.report.*;
import ru.ellen.service.dao.*;

import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public class DbWorker {
    final SimpleJdbcTemplate source;

    private String getSQL(String name) throws IOException {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        MainApp.class.getResourceAsStream(name),
                        StandardCharsets.UTF_8))) {
            return br.lines().collect(Collectors.joining("\n"));
        }
    }

    public void create() throws SQLException, IOException {
        String sql = getSQL("create-database.sql");
        source.statement(stmt -> {
            stmt.execute(sql);
        });
        //fillAllTables();
    }

    /**
     * Заполнение всех таблиц данными.
     */
    public void fillAllTables() throws IOException, SQLException {
        fillAircrafts();
        fillAirports();
        fillBoardingPases();
        fillBookings();
        fillFlights();
        fillSeats();
        fillTicketFlights();
        fillTickets();
    }

    private void fillAircrafts() throws IOException, SQLException {
        AircraftsDao airDao = new AircraftsDao(source);
        String urlStr = "https://storage.yandexcloud.net/airtrans-small/aircrafts.csv";
        URL url = new URL(urlStr);

        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String s;
        ArrayList<Aircrafts> aircrafts = new ArrayList<>();
        while ((s = in.readLine()) != null) {
            String[] info = s.split(",");
            aircrafts.add(new Aircrafts(info[0], strToJsonFormat(info[1], info[2]),
                    Integer.valueOf(info[3])));
        }
        airDao.saveAircrafts(aircrafts);
    }

    private void fillAirports() throws IOException, SQLException {
        AirportsDao airDao = new AirportsDao(source);
        String urlStr = "https://storage.yandexcloud.net/airtrans-small/airports.csv";
        URL url = new URL(urlStr);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String s;
        ArrayList<Airports> airports = new ArrayList<>();
        while ((s = in.readLine()) != null) {
            String[] info = s.split(",");
            airports.add(new Airports(info[0], strToJsonFormat(info[1], info[2]),
                    strToJsonFormat(info[3], info[4]),
                    info[5], info[6]));
        }
        airDao.saveAirports(airports);
    }

    private void fillBoardingPases() throws IOException, SQLException {
        BoardingPassesDao passDao = new BoardingPassesDao(source);
        String urlStr = "https://storage.yandexcloud.net/airtrans-small/boarding_passes.csv";
        URL url = new URL(urlStr);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String s;
        ArrayList<BoardingPasses> passes = new ArrayList<>();
        while ((s = in.readLine()) != null) {
            String[] info = s.split(",");
            passes.add(new BoardingPasses(info[0], Integer.valueOf(info[1]),
                    Integer.valueOf(info[2]), info[3]));
        }
        passDao.saveBoardingPasses(passes);
    }

    private void fillBookings() throws IOException, SQLException {
        BookingsDao passDao = new BookingsDao(source);
        String urlStr = "https://storage.yandexcloud.net/airtrans-small/bookings.csv";
        URL url = new URL(urlStr);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String s;
        ArrayList<Bookings> bookings = new ArrayList<>();
        while ((s = in.readLine()) != null) {
            String[] info = s.split(",");
            Timestamp t = Timestamp.valueOf(info[1].split("\\+")[0]);
            bookings.add(new Bookings(info[0], t, new BigDecimal(info[2])));
        }
        passDao.saveBookings(bookings);
    }

    private void fillFlights() throws IOException, SQLException {
        FlightsDao passDao = new FlightsDao(source);
        String urlStr = "https://storage.yandexcloud.net/airtrans-small/flights.csv";
        URL url = new URL(urlStr);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String s;
        ArrayList<Flights> flights = new ArrayList<>();
        while ((s = in.readLine()) != null) {
            String[] info = s.split(",");
            Timestamp ts1 = (info.length > 8) ? Timestamp.valueOf(info[8].split("\\+")[0]) : null;
            Timestamp ts2 = (info.length > 9) ? Timestamp.valueOf(info[9].split("\\+")[0]) : null;

            flights.add(new Flights(Long.valueOf(info[0]), info[1],
                    Timestamp.valueOf(info[2].split("\\+")[0]),
                    Timestamp.valueOf(info[3].split("\\+")[0]),
                    info[4], info[5], info[6], info[7], ts1, ts2));
        }
        passDao.saveFlights(flights);
    }

    private void fillSeats() throws IOException, SQLException {
        SeatsDao seatsDao = new SeatsDao(source);
        String urlStr = "https://storage.yandexcloud.net/airtrans-small/seats.csv";
        URL url = new URL(urlStr);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String s;
        ArrayList<Seats> seats = new ArrayList<>();
        while ((s = in.readLine()) != null) {
            String[] info = s.split(",");
            seats.add(new Seats(info[0], info[1], info[2]));
        }
        seatsDao.saveSeats(seats);
    }

    private void fillTicketFlights() throws IOException, SQLException {
        TicketFlightsDao seatsDao = new TicketFlightsDao(source);
        String urlStr = "https://storage.yandexcloud.net/airtrans-small/ticket_flights.csv";
        URL url = new URL(urlStr);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String s;
        ArrayList<TicketFlights> ticketFlights = new ArrayList<>();
        while ((s = in.readLine()) != null) {
            String[] info = s.split(",");
            ticketFlights.add(new TicketFlights(info[0], Integer.valueOf(info[1]),
                    info[2], new BigDecimal(info[3])));
        }
        seatsDao.saveTicketFlights(ticketFlights);
    }

    private void fillTickets() throws IOException, SQLException {
        TicketsDao seatsDao = new TicketsDao(source);
        String urlStr = "https://storage.yandexcloud.net/airtrans-small/tickets.csv";
        URL url = new URL(urlStr);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String s;
        ArrayList<Tickets> tickets = new ArrayList<>();
        while ((s = in.readLine()) != null) {
            String[] info = s.split(",");
            String extra = "";
            for (int i = 4; i < info.length - 1; i++) {
                extra += info[i];
            }
            String data = (info.length > 4) ? extra : null;
            tickets.add(new Tickets(info[0], info[1], info[2], info[3], data));
        }
        seatsDao.saveTickets(tickets);
    }

    private String strToJsonFormat(String str1, String str2) {
        return (str1.substring(1) + "," + str2.substring(0, str2.length() - 1))
                .replaceAll("\"\"", "\\\"");
    }

    /**
     * Поиск городов, в которых несколько аэропортов.
     *
     * @return Ответ в специальном формате
     */
    public ArrayList<ReportFormat1> getCitiesWithNumerousAirports()
            throws IOException, SQLException {
        String sql = getSQL("queries/query1.sql");
        ArrayList<ReportFormat1> res = new ArrayList<>();
        source.statement(stmt -> {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                res.add(new ReportFormat1(
                        getRussianCity(rs.getString("city")),
                        rs.getString("airportsList")
                ));
            }
        });
        return res;
    }

    /**
     * Поиск городов, из которых чаще всего отменяли рейсы.
     *
     * @return Ответ в специальном формате
     */
    public ArrayList<ReportFormat2> getCitiesWithCancelledFlights()
            throws IOException, SQLException {
        String sql = getSQL("queries/query2.sql");
        ArrayList<ReportFormat2> res = new ArrayList<>();
        source.statement(stmt -> {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                res.add(new ReportFormat2(
                        getRussianCity(rs.getString("city")),
                        rs.getString("cnt")
                ));
            }
        });
        return res;
    }

    /**
     * Поиск самых коротких маршрутов в городах.
     *
     * @return Ответ в специальном формате
     */
    public ArrayList<ReportFormat3> getShortestPaths() throws IOException, SQLException {
        String sql = getSQL("queries/query3.sql");
        ArrayList<ReportFormat3> res = new ArrayList<>();
        source.statement(stmt -> {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                res.add(new ReportFormat3(
                        getRussianCity(rs.getString("departure")),
                        getRussianCity(rs.getString("arrival")),
                        Long.valueOf(rs.getString("avg_time"))
                ));
            }
        });
        return res;
    }

    /**
     * Поиск количества отмен рейсов по месяцам.
     *
     * @return Ответ в специальном формате
     */
    public ArrayList<ReportFormat4> getCancelledByMonths() throws IOException, SQLException {
        String sql = getSQL("queries/query4.sql");
        ArrayList<ReportFormat4> res = new ArrayList<>();
        source.statement(stmt -> {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int date = Integer.valueOf(rs.getString(1));
                res.add(new ReportFormat4(
                        Month.of(date).toString(),
                        Integer.valueOf(rs.getString("num_cancelled"))
                ));
            }
        });
        return res;
    }

    /**
     * Поиск количества рейсов в Москву и из Москвы по дням недели за весь наблюдаемый период
     *
     * @return Ответ в специальном формате
     */
    public ArrayList<ReportFormat5> getMoscowFlightsByWeekDays() throws IOException, SQLException {
        String sql = getSQL("queries/query5.sql");
        ArrayList<ReportFormat5> res = new ArrayList<>();
        source.statement(stmt -> {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int day = Integer.valueOf(rs.getString(1));
                res.add(new ReportFormat5(
                        DayOfWeek.of(day).toString(),
                        Integer.valueOf(rs.getString("incoming")),
                        Integer.valueOf(rs.getString("outcoming"))
                ));
            }
        });
        return res;
    }

    /**
     * Отмена всех рейсов самолета заданной модели.
     *
     * @param model модель самолета.
     */
    public void doDeleteFlights(String model) throws IOException, SQLException {
        String sql = getSQL("queries/query6.sql");
        source.preparedStatement(sql, stmt -> {
            stmt.setString(1, model);
            stmt.execute();
        });
    }

    /**
     * Отмена рейсов в рамках заданного периода. Подсчет потерь.
     *
     * @param startDate дата начала периода
     * @param endDate   дата окончания периода
     * @return Ответ в специальном формате (потери за каждый день)
     */

    public ArrayList<ReportFormat7> doCancellCovidMoscowFlights(String startDate, String endDate)
            throws IOException, SQLException {
        String sql1 = getSQL("queries/query7_1.sql");
        String sql2 = getSQL("queries/query7_2.sql");
        ArrayList<ReportFormat7> res = new ArrayList<>();
        source.connection(conn -> {
            conn.setAutoCommit(false);
            PreparedStatement stmt1 = conn.prepareStatement(sql1);
            PreparedStatement stmt2 = conn.prepareStatement(sql2);

            // Ищем суммы
            stmt1.setDate(1, Date.valueOf(startDate));
            stmt1.setDate(2, Date.valueOf(endDate));
            stmt2.setDate(1, Date.valueOf(startDate));
            stmt2.setDate(2, Date.valueOf(endDate));

            ResultSet rs = stmt1.executeQuery();
            while (rs.next()) {
                res.add(new ReportFormat7(
                        rs.getString("day_cancelled"),
                        (Double.valueOf(rs.getString("money_loss")).longValue())
                ));
            }

            // Обновляем
            stmt2.execute();
            conn.commit();
        });
        return res;
    }

    /**
     * Добавление нового билета. Проверяется, что такой рейс есть,
     * имеются свободные места,
     * а билет с таким номером еще не был продан.
     *
     * @param ticket         билет на рейс
     * @param booking        бронирование
     * @param flightId       идентификатор рейса
     * @param fareConditions класс обслуживания
     */
    public void addNewTicket(Tickets ticket, Bookings booking, int flightId, String fareConditions)
            throws IOException, SQLException {
        String sql1 = getSQL("queries/query8_1.sql");
        String sql2 = getSQL("queries/query8_2.sql");
        String sql3 = getSQL("queries/query8_3.sql");
        String sql4 = getSQL("queries/query8_4.sql");
        String sql5 = getSQL("queries/query8_5.sql");
        String sql6 = getSQL("queries/query8_6.sql");

        source.connection(conn -> {
            conn.setAutoCommit(false);
            try {
                PreparedStatement stmt1 = conn.prepareStatement(sql1);
                PreparedStatement stmt2 = conn.prepareStatement(sql2);
                PreparedStatement stmt3 = conn.prepareStatement(sql3);
                PreparedStatement stmt4 = conn.prepareStatement(sql4);
                PreparedStatement stmt5 = conn.prepareStatement(sql5);
                PreparedStatement stmt6 = conn.prepareStatement(sql6);

                stmt1.setInt(1, flightId);
                stmt2.setString(1, ticket.getTicketNo());
                stmt3.setString(1, booking.getBookRef());

                ResultSet rs1 = stmt1.executeQuery();
                if (!rs1.next()) {
                    throw new Exception();
                }
                String aircraftCode = rs1.getString("aircraft_code");
                ResultSet rs2 = stmt2.executeQuery();
                rs2.next();
                ResultSet rs3 = stmt3.executeQuery();
                rs3.next();
                if ((rs2.getInt(1) > 0) || (rs3.getInt(1) > 0)) {
                    throw new Exception();
                }

                stmt4.setString(1, aircraftCode);
                stmt4.setString(2, fareConditions);
                ResultSet rs4 = stmt4.executeQuery();
                rs4.next();
                stmt5.setInt(1, flightId);
                stmt5.setString(2, fareConditions);
                ResultSet rs5 = stmt5.executeQuery();
                rs5.next();

                // Проверяем наличие свободных мест
                if (rs4.getInt(1) < rs5.getInt(1)) {
                    throw new Exception();
                }

                // Добавляем данные в таблички
                stmt6.setString(1, ticket.getTicketNo());
                stmt6.setInt(2, flightId);
                stmt6.setString(3, fareConditions);
                stmt6.setBigDecimal(4, booking.getTotalAmount());
                stmt6.execute();

                // И для разнообразия другой вариант сохранения в табличку
                BookingsDao bookingsDao = new BookingsDao(source);
                bookingsDao.saveBookings(new ArrayList<>(Collections.singletonList(booking)));
                TicketsDao ticketsDao = new TicketsDao(source);
                ticketsDao.saveTickets(new ArrayList<>(Collections.singletonList(ticket)));

                conn.commit();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("There is an error in your query");
                conn.rollback();
                conn.setAutoCommit(true);
            }
            conn.setAutoCommit(true);
        });
    }

    private String getRussianCity(String str) {
        String city = null;
        try {
            JSONObject js = new JSONObject(str);
            city = js.getString("en");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return city;
    }
}
