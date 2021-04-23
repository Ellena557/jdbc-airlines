package ru.ellen;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import ru.ellen.report.ReportFormat;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class GraphGenerator<T extends ReportFormat> {
    private JFreeChart chart;

    public void generateGraph(ArrayList<T> data, String chartName,
                              ArrayList<String> headers, String filename) throws IOException {
        FileOutputStream fileOutputStream =
                new FileOutputStream(new File(filename));
        CategoryDataset dataset = makeDataset(data, headers);
        initChart(chartName, headers, dataset);
        ChartUtilities.writeChartAsPNG(fileOutputStream, chart, 1800, 1000);
    }

    private void initChart(String chartName, ArrayList<String> headers, CategoryDataset dataset) {
        // For task 5 - special case
        String yLabel = (headers.size() == 2) ? headers.get(1) : "Number of flights";
        boolean legend = (headers.size() == 3);
        chart = ChartFactory.createBarChart(chartName, headers.get(0),
                yLabel, dataset, PlotOrientation.VERTICAL,
                legend, true, false);
        Plot plot = chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);

        CategoryPlot categoryPlot = chart.getCategoryPlot();
        categoryPlot.setDomainGridlinesVisible(true);
        categoryPlot.setDomainGridlinePaint(Color.gray);
        categoryPlot.setRangeGridlinesVisible(true);
        categoryPlot.setRangeGridlinePaint(Color.gray);
        BarRenderer barRenderer = (BarRenderer) categoryPlot.getRenderer();
        barRenderer.setMaximumBarWidth(10);
        barRenderer.setShadowVisible(false);
    }

    private CategoryDataset makeDataset(ArrayList<T> data, ArrayList<String> headers) {
        DefaultCategoryDataset res = new DefaultCategoryDataset();
        for (int i = 0; i < data.size(); i++) {
            switch (headers.size()) {
                case 2:
                    // tasks 4 and 7
                    res.addValue(Integer.valueOf(data.get(i).getItems().get(1).toString()),
                            "",
                            data.get(i).getItems().get(0).toString());
                    break;
                case 3:
                    // task 5: from/to Moscow
                    res.addValue(Integer.valueOf(data.get(i).getItems().get(1).toString()),
                            headers.get(1),
                            data.get(i).getItems().get(0).toString());
                    res.addValue(Integer.valueOf(data.get(i).getItems().get(2).toString()),
                            headers.get(2),
                            data.get(i).getItems().get(0).toString());
                    break;
            }
        }
        return res;
    }
}
