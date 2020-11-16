package pl.lodz.p.mobi.covidapp.viewmodel;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pl.lodz.p.mobi.covidapp.R;
import pl.lodz.p.mobi.covidapp.json.data.DataTypeEnum;
import pl.lodz.p.mobi.covidapp.json.JsonDataParser;

public class StartFragmentViewModel extends ViewModel {
    private final JsonDataParser jsonDataParser = new JsonDataParser();
    private Map<String, Integer> stats = new TreeMap<>();

    public void setStats(int daysConsideredNumber, DataTypeEnum dataType) {
        stats = jsonDataParser.readStatistics(dataType, daysConsideredNumber);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public List<String> getStatsKeys() {
        return new ArrayList<>(stats.keySet());
    }

    public List<Integer> getStatsValues() {
        return new ArrayList<>(stats.values());
    }
}
