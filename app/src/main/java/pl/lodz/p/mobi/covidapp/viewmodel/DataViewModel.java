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
    private Map<String, Integer> countryDeathsStats = new TreeMap<>();
    private Map<String, Integer> countryRecoveredStats = new TreeMap<>();
    private Map<String, Integer> countryConfirmedStats = new TreeMap<>();
    private Map<String, CountyModel> countyStats = new TreeMap<>();

    public void setCountryStats(int daysConsideredNumber) {
        countryDeathsStats = JsonDataParser.readTotalCountryStatistics(DataTypeEnum.DEATHS, daysConsideredNumber);
        countryRecoveredStats = JsonDataParser.readTotalCountryStatistics(DataTypeEnum.CONFIRMED, daysConsideredNumber);
        countryConfirmedStats = JsonDataParser.readTotalCountryStatistics(DataTypeEnum.RECOVERED, daysConsideredNumber);
    }

    public void setCountyStats() {
        countyStats = JsonDataParser.readCountyStatistics();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public Map<String, Integer> getCountryDeathsStats() {
        return countryDeathsStats;
    }

    public Map<String, Integer> getCountryRecoveredStats() {
        return countryRecoveredStats;
    }

    public Map<String, Integer> getCountryConfirmedStats() {
        return countryConfirmedStats;
    }

    public Map<String, CountyModel> getCountyStats() {
        if(countyStats.isEmpty()) setCountyStats();
        return new TreeMap<>(countyStats);
    }
}
