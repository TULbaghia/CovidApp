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
import androidx.lifecycle.ViewModelStoreOwner;

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

    public StartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataViewModel = new ViewModelProvider((ViewModelStoreOwner) this.getContext()).get(DataViewModel.class);
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
        SQLiteHelper sqLiteHelper = new SQLiteHelper(getContext());
        Map<String, String> config = sqLiteHelper.getConfig();
        dataViewModel.setCountryStats(getResources().getInteger(R.integer.days_considered));
        if (config.get("DATA_TYPE").equals("DEATHS")) {
            setChartOptions(config, dataViewModel.getCountryDeathsStats(), getString(R.string.deaths));
        } else if (config.get("DATA_TYPE").equals("RECOVERED")) {
            setChartOptions(config, dataViewModel.getCountryRecoveredStats(), getString(R.string.recovered));
        } else {
            setChartOptions(config, dataViewModel.getCountryConfirmedStats(), getString(R.string.confirmed));
        }
        sqLiteHelper.close();
    }

    private void initStatsTextViews() {
        String dataFromDay = new ArrayList<>(dataViewModel.getCountryDeathsStats().entrySet()).get(dataViewModel.getCountryDeathsStats().size() - 2).getKey();
        dataFromDay = dataFromDay.replace('-', '/');

        List<Integer> confirmed = new ArrayList<>(dataViewModel.getCountryConfirmedStats().values());
        int resultConfirmed = confirmed.get(confirmed.size() - 2) - confirmed.get(confirmed.size() - 3);

        List<Integer> recovered = new ArrayList<>(dataViewModel.getCountryRecoveredStats().values());
        int resultRecovered = recovered.get(confirmed.size() - 2) - recovered.get(confirmed.size() - 3);

        List<Integer> deaths = new ArrayList<>(dataViewModel.getCountryDeathsStats().values());
        int resultDeaths = deaths.get(confirmed.size() - 2) - deaths.get(confirmed.size() - 3);

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

    public void setChartOptions(Map<String, String> config, Map<String, Integer> data, String label) {
        int daysConsidered = Integer.parseInt(config.get("DAYS_CONSIDERED"));
        String chartType = config.get("CHART_TYPE");
        List<String> keySet = new ArrayList<>(data.keySet());
        List<Integer> values = new ArrayList<>(data.values());

        if (config.get("CHART_TYPE").equals("PER_DAY")) {
            for (int i = values.size() - 1; i > 0; i--) {
                values.set(i, values.get(i) - values.get(i - 1));
            }
        }

        barChartWrapper.setBarEntries(values.subList(values.size() - daysConsidered - 1, values.size() - 1), label, chartType);
        barChartWrapper.formatXAxis(keySet.subList(keySet.size() - daysConsidered - 1, keySet.size() - 1));
        barChartWrapper.getBarChart().notifyDataSetChanged();
        barChartWrapper.getBarChart().invalidate();
    }
}