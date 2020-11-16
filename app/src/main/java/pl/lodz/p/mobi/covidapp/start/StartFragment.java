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
import pl.lodz.p.mobi.covidapp.viewmodel.StartFragmentViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class StartFragment extends Fragment {

    StartFragmentViewModel startFragmentViewModel = null;
    BarChartWrapper barChartWrapper = null;

    public StartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startFragmentViewModel = new ViewModelProvider(this).get(StartFragmentViewModel.class);
    }

    private void initChartButtonsListeners(View view) {
        FloatingActionButton showConfirmedChartButton = view.findViewById(R.id.showConfirmedChartButton);
        FloatingActionButton showDeathsChartButton = view.findViewById(R.id.showDeathsChartButton);
        FloatingActionButton showRecoveredChartButton = view.findViewById(R.id.showRecoveredChartButton);
        showConfirmedChartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChartOptions(DataTypeEnum.CONFIRMED, getString(R.string.confirmed));
            }
        });

        showDeathsChartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChartOptions(DataTypeEnum.DEATHS, getString(R.string.deaths));
            }
        });

        showRecoveredChartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChartOptions(DataTypeEnum.RECOVERED, getString(R.string.recovered));
            }
        });
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
        startFragmentViewModel.setStats(getResources().getInteger(R.integer.days_considered), dataType);
        barChartWrapper.setBarEntries(startFragmentViewModel.getStatsValues(), label);
        barChartWrapper.formatXAxis(startFragmentViewModel.getStatsKeys());
        barChartWrapper.getBarChart().notifyDataSetChanged();
        barChartWrapper.getBarChart().invalidate();
    }


}