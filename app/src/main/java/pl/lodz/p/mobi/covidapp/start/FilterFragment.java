package pl.lodz.p.mobi.covidapp.start;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.Map;

import pl.lodz.p.mobi.covidapp.R;
import pl.lodz.p.mobi.covidapp.persistance.SQLiteHelper;

public class FilterFragment extends DialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button dismiss = view.findViewById(R.id.dismissButton);
        dismiss.setOnClickListener(v -> dismiss());
        initChartButtonsListeners();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        ((StartFragment) getTargetFragment()).initConfig();
    }

    private void initChartButtonsListeners() {
        SQLiteHelper sqLiteHelper = new SQLiteHelper(getContext());
        Map<String, String> config = sqLiteHelper.getConfig();
        initSpinner(sqLiteHelper, config.get("DAYS_CONSIDERED"));

        Button showConfirmedChartButton = requireView().findViewById(R.id.showConfirmedChartButton);
        Button showDeathsChartButton = requireView().findViewById(R.id.showDeathsChartButton);
        Button showRecoveredChartButton = requireView().findViewById(R.id.showRecoveredChartButton);
        Button agregateButton = requireView().findViewById(R.id.agregateButton);
        Button lastDayButton = requireView().findViewById(R.id.lastDayButton);

        showConfirmedChartButton.setOnClickListener(v -> setButtonConfig(() -> sqLiteHelper.updateConfig("DATA_TYPE", "CONFIRMED"), (Button) v, R.id.dataLayout));
        showDeathsChartButton.setOnClickListener(v -> setButtonConfig(() -> sqLiteHelper.updateConfig("DATA_TYPE", "DEATHS"), (Button) v, R.id.dataLayout));
        showRecoveredChartButton.setOnClickListener(v -> setButtonConfig(() -> sqLiteHelper.updateConfig("DATA_TYPE", "RECOVERED"), (Button) v, R.id.dataLayout));

        agregateButton.setOnClickListener(v -> setButtonConfig(() -> sqLiteHelper.updateConfig("CHART_TYPE", "AGGREGATED"), (Button) v, R.id.aggregateLayout));
        lastDayButton.setOnClickListener(v -> setButtonConfig(() -> sqLiteHelper.updateConfig("CHART_TYPE", "PER_DAY"), (Button) v, R.id.aggregateLayout));

        if (config.get("CHART_TYPE").equals("AGGREGATED")) {
            agregateButton.performClick();
        } else {
            lastDayButton.performClick();
        }

        if (config.get("DATA_TYPE").equals("DEATHS")) {
            showDeathsChartButton.performClick();
        } else if (config.get("DATA_TYPE").equals("RECOVERED")) {
            showRecoveredChartButton.performClick();
        } else {
            showConfirmedChartButton.performClick();
        }
    }

    public void setButtonConfig(Runnable runnable, Button button, int vg) {
        runnable.run();
        ViewGroup viewGroup = requireView().findViewById(vg);
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            if (viewGroup.getChildAt(i) instanceof Button) {
                ((Button) viewGroup.getChildAt(i)).setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.blue_light, null)));
            }
        }
        button.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green_light, null)));
    }

    private void initSpinner(SQLiteHelper sqLiteHelper, String value) {
        Spinner spinner = requireView().findViewById(R.id.filterSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(), R.array.range, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        if (value.equals("7")) {
            spinner.setSelection(0);
        } else if (value.equals("14")) {
            spinner.setSelection(1);
        } else {
            spinner.setSelection(2);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    sqLiteHelper.updateConfig("DAYS_CONSIDERED", "7");
                } else if (position == 1) {
                    sqLiteHelper.updateConfig("DAYS_CONSIDERED", "14");
                } else {
                    sqLiteHelper.updateConfig("DAYS_CONSIDERED", "30");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}