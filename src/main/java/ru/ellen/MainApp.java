package ru.ellen;

import ru.ellen.domain.Bookings;
import ru.ellen.domain.Tickets;
import ru.ellen.report.*;
import ru.ellen.service.db.DbWorker;
import ru.ellen.service.db.SimpleJdbcTemplate;
import org.h2.jdbcx.JdbcConnectionPool;

import javax.sql.DataSource;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class MainApp {
    private static DataSource s = JdbcConnectionPool.create("jdbc:h2:mem:database;DB_CLOSE_DELAY=-1",
            "", "");
    private static SimpleJdbcTemplate source = new SimpleJdbcTemplate(
            s);

    public static void main(String[] args) throws IOException, SQLException {
        DbWorker db = new DbWorker(source);
        db.create();
        db.fillAllTables();

        if (args.length == 0) {
            return;
        }
        switch (args[0]) {
            case "query1":
                ArrayList<ReportFormat1> data1 = db.getCitiesWithNumerousAirports();
                ReportGenerator<ReportFormat1> reportGenerator1 = new ReportGenerator<>();
                reportGenerator1.generateReport(data1, "artifacts/result_1.xls",
                        ReportFormat1.headers);
                break;

            case "query2":
                ArrayList<ReportFormat2> data2 = db.getCitiesWithCancelledFlights();
                ReportGenerator<ReportFormat2> reportGenerator2 = new ReportGenerator<>();
                reportGenerator2.generateReport(data2, "artifacts/result_2.xls",
                        ReportFormat2.headers);
                break;

            case "query3":
                ArrayList<ReportFormat3> data3 = db.getShortestPaths();
                ReportGenerator<ReportFormat3> reportGenerator3 = new ReportGenerator<>();
                reportGenerator3.generateReport(data3, "artifacts/result_3.xls",
                        ReportFormat3.headers);
                break;

            case "query4":
                ArrayList<ReportFormat4> data4 = db.getCancelledByMonths();
                ReportGenerator<ReportFormat4> reportGenerator4 = new ReportGenerator<>();
                reportGenerator4.generateReport(data4, "artifacts/result_4.xls",
                        ReportFormat4.headers);
                GraphGenerator<ReportFormat4> graphGenerator4 = new GraphGenerator<>();
                graphGenerator4.generateGraph(data4, "Cancelled flights in months",
                        ReportFormat4.headers, "artifacts/graph_4.png");
                break;

            case "query5":
                ArrayList<ReportFormat5> data5 = db.getMoscowFlightsByWeekDays();
                ReportGenerator<ReportFormat5> reportGenerator5 = new ReportGenerator<>();
                reportGenerator5.generateReport(data5, "artifacts/result_5.xls",
                        ReportFormat5.headers);
                GraphGenerator<ReportFormat5> graphGenerator5 = new GraphGenerator<>();
                graphGenerator5.generateGraph(data5, "Moscow flights in week days",
                        ReportFormat5.headers, "artifacts/graph_5.png");
                break;

            case "query6":
                if (args.length < 2) {
                    System.out.println("Not enough arguments");
                    break;
                }
                db.doDeleteFlights(args[1]);
                // nothing to export here
                break;

            case "query7":
                if (args.length < 3) {
                    System.out.println("Not enough arguments");
                    break;
                }
                ArrayList<ReportFormat7> data7 = db.doCancellCovidMoscowFlights(args[1], args[2]);
                ReportGenerator<ReportFormat7> reportGenerator7 = new ReportGenerator<>();
                reportGenerator7.generateReport(data7, "artifacts/result_7.xls",
                        ReportFormat7.headers);
                GraphGenerator<ReportFormat7> graphGenerator7 = new GraphGenerator<>();
                graphGenerator7.generateGraph(data7, "Lost money amounts",
                        ReportFormat7.headers, "artifacts/graph_7.png");
                break;

            case "query8":
                if (args.length < 10) {
                    System.out.println("Not enough arguments");
                    System.out.println("You need to identify 9 more arguments: " +
                            "ticketNo, bookRef, passengerId, passengerName, contactData, " +
                            "bookDate(yyyy-mm-dd), totalAmount, flightId, fareConditions");
                    break;
                }

                /* Можно обойтись и без отдельного вынесения в переменные,
                но их ввожу, чтобы было понятно, что должен означать каждый параметр.
                 */

                String ticketNo = args[1];
                String bookRef = args[2];
                String passengerId = args[3];
                String passengerName = args[4];
                String contactData = args[5];
                String bookDate = args[6];
                String totalAmount = args[7];
                String flightId = args[8];
                String fareConditions = args[9];
                Tickets myTicket = new Tickets(ticketNo, bookRef, passengerId,
                        passengerName, contactData);
                Bookings myBook = new Bookings(bookRef,
                        new Timestamp(Date.valueOf(bookDate).getTime()),
                        new BigDecimal(totalAmount));
                db.addNewTicket(myTicket, myBook, Integer.valueOf(flightId), fareConditions);
                break;
        }
    }
}
