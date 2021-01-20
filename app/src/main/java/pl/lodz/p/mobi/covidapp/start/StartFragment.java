package pl.lodz.p.mobi.covidapp.start;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import java.util.Map;

import pl.lodz.p.mobi.covidapp.R;
import pl.lodz.p.mobi.covidapp.persistance.SQLiteHelper;
import pl.lodz.p.mobi.covidapp.start.utils.BarChartWrapper;
import pl.lodz.p.mobi.covidapp.viewmodel.DataViewModel;

public class StartFragment extends Fragment {
    DataViewModel dataViewModel = null;
    BarChartWrapper barChartWrapper = null;

    public StartFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_start, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataViewModel = new ViewModelProvider((ViewModelStoreOwner) this.getContext()).get(DataViewModel.class);
        barChartWrapper = new BarChartWrapper(view.findViewById(R.id.barChart));
        initConfig();
        initFilters();
        setBottomText();
    }

    public void initConfig() {
        SQLiteHelper sqLiteHelper = new SQLiteHelper(getContext());
        Map<String, String> config = sqLiteHelper.getConfig();
        dataViewModel.setCountryStats(getResources().getInteger(R.integer.days_considered));
        if (config.get("DATA_TYPE").equals("DEATHS")) {
            barChartWrapper.setChartOptions(config, dataViewModel.getCountryDeathsStats(), getString(R.string.deaths));
        } else if (config.get("DATA_TYPE").equals("RECOVERED")) {
            barChartWrapper.setChartOptions(config, dataViewModel.getCountryRecoveredStats(), getString(R.string.recovered));
        } else {
            barChartWrapper.setChartOptions(config, dataViewModel.getCountryConfirmedStats(), getString(R.string.confirmed));
        }
        sqLiteHelper.close();
    }

    private void initFilters() {
        requireView().findViewById(R.id.filterButton).setOnClickListener(view1 -> {
            DialogFragment dialogFragment = new FilterFragment();
            dialogFragment.setTargetFragment(this, 1);
            dialogFragment.show(getParentFragmentManager(), "Filter fragment");
        });
    }

    private void setBottomText() {
        ((TextView) requireView().findViewById(R.id.statsLabelTextView)).setText(getString(R.string.last_day, dataViewModel.getStartStatsDay()));
        ((TextView) requireView().findViewById(R.id.confirmedTextView)).setText(getString(R.string.last_day_confirmed, dataViewModel.getStartStats().getLeft()));
        ((TextView) requireView().findViewById(R.id.recoveredTextView)).setText(getString(R.string.last_day_recovered, dataViewModel.getStartStats().getMiddle()));
        ((TextView) requireView().findViewById(R.id.deathsTextView)).setText(getString(R.string.last_day_deaths, dataViewModel.getStartStats().getRight()));
    }
}