package pl.lodz.p.mobi.covidapp.start;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pl.lodz.p.mobi.covidapp.R;
import pl.lodz.p.mobi.covidapp.persistance.SQLiteHelper;
import pl.lodz.p.mobi.covidapp.start.chart.BarChartWrapper;
import pl.lodz.p.mobi.covidapp.viewmodel.DataViewModel;

public class StartFragment extends Fragment {

    DataViewModel dataViewModel = null;
    BarChartWrapper barChartWrapper = null;
    SQLiteHelper sqLiteHelper = null;

    public StartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        sqLiteHelper = new SQLiteHelper(getContext());
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
        initConfig();
        initStatsTextViews();
        initFilters();
    }

    public void initConfig() {
        Map<String, String> config = sqLiteHelper.getConfig();
        dataViewModel.setCountryStats(Integer.parseInt(config.get("DAYS_CONSIDERED")));
        if (config.get("DATA_TYPE").equals("DEATHS")) {
            setChartOptions(dataViewModel.getCountryDeathsStats(), getString(R.string.deaths));
        } else if (config.get("DATA_TYPE").equals("RECOVERED")) {
            setChartOptions(dataViewModel.getCountryDeathsStats(), getString(R.string.recovered));
        } else {
            setChartOptions(dataViewModel.getCountryDeathsStats(), getString(R.string.confirmed));
        }
    }

    private void initStatsTextViews() {
        String dataFromDay = new ArrayList<>(dataViewModel.getCountryDeathsStats().entrySet()).get(dataViewModel.getCountryDeathsStats().size() - 1).getKey();
        dataFromDay = dataFromDay.replace('-', '/');

        List<Integer> confirmed = new ArrayList<>(dataViewModel.getCountryConfirmedStats().values());
        int resultConfirmed = confirmed.get(confirmed.size() - 1) - confirmed.get(confirmed.size() - 2);

        List<Integer> recovered = new ArrayList<>(dataViewModel.getCountryRecoveredStats().values());
        int resultRecovered = recovered.get(confirmed.size() - 1) - recovered.get(confirmed.size() - 2);

        List<Integer> deaths = new ArrayList<>(dataViewModel.getCountryDeathsStats().values());
        int resultDeaths = deaths.get(confirmed.size() - 1) - deaths.get(confirmed.size() - 2);

        ((TextView) requireView().findViewById(R.id.statsLabelTextView)).setText(getString(R.string.last_day, dataFromDay));
        ((TextView) requireView().findViewById(R.id.confirmedTextView)).setText(getString(R.string.last_day_confirmed, resultConfirmed));
        ((TextView) requireView().findViewById(R.id.recoveredTextView)).setText(getString(R.string.last_day_recovered, resultRecovered));
        ((TextView) requireView().findViewById(R.id.deathsTextView)).setText(getString(R.string.last_day_deaths, resultDeaths));
    }

    private void initFilters() {
        FragmentManager fm = getParentFragmentManager();

        requireView().findViewById(R.id.filterButton).setOnClickListener(view1 -> {
            DialogFragment dialogFragment = new FilterFragment();
            dialogFragment.setTargetFragment(this, 1);
            dialogFragment.show(fm, "Sample Fragment");
        });
    }

    public void setChartOptions(Map<String, Integer> data, String label) {
        barChartWrapper.setBarEntries(new ArrayList<>(data.values()), label);
        barChartWrapper.formatXAxis(new ArrayList<>(data.keySet()));
        barChartWrapper.getBarChart().notifyDataSetChanged();
        barChartWrapper.getBarChart().invalidate();
    }
}