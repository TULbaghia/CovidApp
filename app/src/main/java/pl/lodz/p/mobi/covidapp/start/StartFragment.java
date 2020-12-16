package pl.lodz.p.mobi.covidapp.start;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import pl.lodz.p.mobi.covidapp.R;
import pl.lodz.p.mobi.covidapp.start.chart.BarChartWrapper;
import pl.lodz.p.mobi.covidapp.viewmodel.DataViewModel;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_start, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        barChartWrapper = new BarChartWrapper(view.findViewById(R.id.barChart));
        dataViewModel.setCountryStats(getResources().getInteger(R.integer.days_considered));
        initChartButtonsListeners();
        initStatsTextViews();
        setDefaultChart();
    }


    private void initChartButtonsListeners() {
        FloatingActionButton showConfirmedChartButton = requireView().findViewById(R.id.showConfirmedChartButton);
        FloatingActionButton showDeathsChartButton = requireView().findViewById(R.id.showDeathsChartButton);
        FloatingActionButton showRecoveredChartButton = requireView().findViewById(R.id.showRecoveredChartButton);

        showConfirmedChartButton.setOnClickListener(v -> setChartOptions(dataViewModel.getCountryConfirmedStats(), getString(R.string.confirmed)));
        showDeathsChartButton.setOnClickListener(v -> setChartOptions(dataViewModel.getCountryDeathsStats(), getString(R.string.deaths)));
        showRecoveredChartButton.setOnClickListener(v -> setChartOptions(dataViewModel.getCountryRecoveredStats(), getString(R.string.recovered)));
    }

    private void initStatsTextViews() {
        String dataFromDay = new ArrayList<>(dataViewModel.getCountryDeathsStats().entrySet()).get(dataViewModel.getCountryDeathsStats().size() - 1).getKey();
        dataFromDay = dataFromDay.replace('-', '/');
        int confirmed = new ArrayList<>(dataViewModel.getCountryConfirmedStats().entrySet()).get(dataViewModel.getCountryConfirmedStats().size() - 1).getValue();
        int recovered = new ArrayList<>(dataViewModel.getCountryRecoveredStats().entrySet()).get(dataViewModel.getCountryRecoveredStats().size() - 1).getValue();
        int dead = new ArrayList<>(dataViewModel.getCountryDeathsStats().entrySet()).get(dataViewModel.getCountryDeathsStats().size() - 1).getValue();

        ((TextView) requireView().findViewById(R.id.statsLabelTextView)).setText(getString(R.string.last_day, dataFromDay));
        ((TextView) requireView().findViewById(R.id.confirmedTextView)).setText(getString(R.string.last_day_confirmed, confirmed));
        ((TextView) requireView().findViewById(R.id.recoveredTextView)).setText(getString(R.string.last_day_recovered, recovered));
        ((TextView) requireView().findViewById(R.id.deathsTextView)).setText(getString(R.string.last_day_deaths, dead));
    }

    private void setDefaultChart() {
        requireView().findViewById(R.id.showDeathsChartButton).callOnClick();
    }

    public void setChartOptions(Map<String, Integer> data, String label) {
        barChartWrapper.setBarEntries(new ArrayList<>(data.values()), label);
        barChartWrapper.formatXAxis(new ArrayList<>(data.keySet()));
        barChartWrapper.getBarChart().notifyDataSetChanged();
        barChartWrapper.getBarChart().invalidate();
    }
}