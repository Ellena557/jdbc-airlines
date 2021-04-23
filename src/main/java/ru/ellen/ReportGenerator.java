package ru.ellen;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.ellen.report.ReportFormat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ReportGenerator<T extends ReportFormat> {
    private XSSFCellStyle cellStyle;
    private XSSFCellStyle headerCellStyle;
    private XSSFWorkbook report;

    public void generateReport(ArrayList<T> data, String name, ArrayList<String> headers) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(new File(name));
        report = new XSSFWorkbook();
        XSSFSheet sheet = report.createSheet("results");
        sheet.createFreezePane(0, 1);
        createCellTypes();

        int numColumns = headers.size();

        // add headers
        XSSFRow headRow = sheet.createRow(0);
        for (int i = 0; i < numColumns; i++) {
            Cell cell = headRow.createCell(i);
            cell.setCellValue(headers.get(i));
            cell.setCellStyle(headerCellStyle);
        }

        // add data from query
        for (int i = 0; i < data.size(); i++) {
            XSSFRow curRow = sheet.createRow(i + 1);
            for (int j = 0; j < numColumns; j++) {
                Cell cell = curRow.createCell(j);
                cell.setCellValue(data.get(i).getItems().get(j).toString());
                cell.setCellStyle(cellStyle);
            }
        }

        for (int i = 0; i < numColumns; ++i) {
            sheet.autoSizeColumn(i);
        }

        report.write(fileOutputStream);
        fileOutputStream.close();
    }

    private void createCellTypes() {
        cellStyle = report.createCellStyle();
        Font font = report.createFont();
        font.setFontHeightInPoints((short) 14);
        cellStyle.setFont(font);

        headerCellStyle = report.createCellStyle();
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerCellStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
        font = report.createFont();
        font.setBold(true);
        font.setColor(HSSFColor.INDIGO.index);
        font.setFontHeightInPoints((short) 14);
        headerCellStyle.setFont(font);
    }
}
