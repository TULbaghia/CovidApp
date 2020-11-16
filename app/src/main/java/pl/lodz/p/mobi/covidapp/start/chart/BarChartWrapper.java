package pl.lodz.p.mobi.covidapp.start.chart;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class BarChartWrapper {
    private final BarChart barChart;

    public BarChartWrapper(BarChart viewBarChart) {
        barChart = viewBarChart;
    }

    public void formatXAxis(List<String> descriptions) {
        XAxis bottomAxis = barChart.getXAxis();
        bottomAxis.setValueFormatter(new IndexAxisValueFormatter(descriptions));
        bottomAxis.setLabelCount(descriptions.size());
    }

    public void setBarEntries(List<Integer> values, String label) {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        int i = 0;
        for (Integer value : values) {
            barEntries.add(new BarEntry(i, value));
            i++;
        }
        BarDataSet barDataSet = new BarDataSet(barEntries, label);
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        barChart.animateY(2000);
    }

    public BarChart getBarChart() {
        return barChart;
    }
}
