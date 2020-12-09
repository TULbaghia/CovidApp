package pl.lodz.p.mobi.covidapp.viewmodel;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pl.lodz.p.mobi.covidapp.json.data.DataTypeEnum;
import pl.lodz.p.mobi.covidapp.json.JsonDataParser;
import pl.lodz.p.mobi.covidapp.json.data.model.CountyModel;

public class DataViewModel extends ViewModel {
    private Map<String, Integer> countryStats = new TreeMap<>();
    private Map<String, CountyModel> countyStats = new TreeMap<>();

    public void setCountryStats(int daysConsideredNumber, DataTypeEnum dataType) {
        countryStats = JsonDataParser.readTotalCountryStatistics(dataType, daysConsideredNumber);
    }

    public void setCountyStats() {
        countyStats = JsonDataParser.readCountyStatistics();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public List<String> getCountryStatsKeys() {
        return new ArrayList<>(countryStats.keySet());
    }

    public List<Integer> getCountryStatsValues() {
        return new ArrayList<>(countryStats.values());
    }

    public Map<String, CountyModel> getCountyStats() {
        if(countyStats.isEmpty()) setCountyStats();
        return new TreeMap<>(countyStats);
    }
}
