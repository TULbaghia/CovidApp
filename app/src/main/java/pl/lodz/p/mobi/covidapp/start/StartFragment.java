package pl.lodz.p.mobi.covidapp.start;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import pl.lodz.p.mobi.covidapp.R;
import pl.lodz.p.mobi.covidapp.json.data.DataTypeEnum;
import pl.lodz.p.mobi.covidapp.start.chart.BarChartWrapper;
import pl.lodz.p.mobi.covidapp.viewmodel.DataViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class StartFragment extends Fragment {

    DataViewModel dataViewModel = null;
    BarChartWrapper barChartWrapper = null;

    public StartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
    }

    private void initChartButtonsListeners(View view) {
        FloatingActionButton showConfirmedChartButton = view.findViewById(R.id.showConfirmedChartButton);
        FloatingActionButton showDeathsChartButton = view.findViewById(R.id.showDeathsChartButton);
        FloatingActionButton showRecoveredChartButton = view.findViewById(R.id.showRecoveredChartButton);
        showConfirmedChartButton.setOnClickListener(view13 -> setChartOptions(DataTypeEnum.CONFIRMED, getString(R.string.confirmed)));

        showDeathsChartButton.setOnClickListener(view1 -> setChartOptions(DataTypeEnum.DEATHS, getString(R.string.deaths)));

        showRecoveredChartButton.setOnClickListener(view12 -> setChartOptions(DataTypeEnum.RECOVERED, getString(R.string.recovered)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        barChartWrapper = new BarChartWrapper(view.findViewById(R.id.barChart));
        initChartButtonsListeners(view);
        setDefaultChart(view);
        return view;
    }

    private void setDefaultChart(View view) {
        view.findViewById(R.id.showDeathsChartButton).performClick();
    }

    public void setChartOptions(DataTypeEnum dataType, String label) {
        dataViewModel.setCountryStats(getResources().getInteger(R.integer.days_considered), dataType);
        barChartWrapper.setBarEntries(dataViewModel.getCountryStatsValues(), label);
        barChartWrapper.formatXAxis(dataViewModel.getCountryStatsKeys());
        barChartWrapper.getBarChart().notifyDataSetChanged();
        barChartWrapper.getBarChart().invalidate();
    }


}