package pl.lodz.p.mobi.covidapp.start.chart;

import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import java.util.ArrayList;
import java.util.List;

import pl.lodz.p.mobi.covidapp.R;

public class BarChartWrapper {
    private final BarChart barChart;

    public BarChartWrapper(BarChart viewBarChart) {
        barChart = viewBarChart;
    }

    public void formatXAxis(List<String> descriptions) {
        XAxis bottomAxis = barChart.getXAxis();
        bottomAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        bottomAxis.setValueFormatter(new IndexAxisValueFormatter(descriptions));
        bottomAxis.setLabelCount(descriptions.size());
        bottomAxis.setTextSize(11f);
    }

    public void setBarEntries(List<Integer> values, String label, String chartType) {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        int i = 0;
        for (Integer value : values) {
            barEntries.add(new BarEntry(i, value));
            i++;
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, label);
        if (values.size() > 7) {
            barDataSet.setDrawValues(false);
        }

        barDataSet.setColor(Color.parseColor("#5BC0DE"));
        barDataSet.setBarBorderColor(Color.BLACK);
        barDataSet.setBarBorderWidth(.5f);

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        barChart.animateY(1000);
        barChart.getDescription().setEnabled(false);
        barChart.getBarData().setValueTextSize(10f);
        barChart.setTouchEnabled(false);
        barChart.setExtraBottomOffset(15f);
        barChart.setExtraLeftOffset(5f);
        barChart.setExtraRightOffset(5f);

        YAxis yAxisRight = barChart.getAxisRight();
        YAxis yAxisLeft = barChart.getAxisLeft();
        if (chartType.equals("AGGREGATED")) {
            yAxisRight.setAxisMinimum(values.get(0) - (values.get(1) - values.get(0)));
            yAxisLeft.setAxisMinimum(values.get(0) - (values.get(1) - values.get(0)));
        } else {
            yAxisRight.setAxisMinimum(0);
            yAxisLeft.setAxisMinimum(0);
        }
        if (values.size() != 7) {
            barChart.getXAxis().setGranularity(4);
        } else {
            barChart.getXAxis().setGranularity(1);
        }


        yAxisRight.setTextSize(13f);
        yAxisLeft.setTextSize(13f);

        Legend legend = barChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setTextSize(13f);
    }

    public BarChart getBarChart() {
        return barChart;
    }
}
