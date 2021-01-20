package pl.lodz.p.mobi.covidapp.viewmodel;

import androidx.lifecycle.ViewModel;

import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pl.lodz.p.mobi.covidapp.json.data.DataTypeEnum;
import pl.lodz.p.mobi.covidapp.json.JsonDataParser;

public class DataViewModel extends ViewModel {
    private Map<String, Integer> countryDeathsStats = new TreeMap<>();
    private Map<String, Integer> countryRecoveredStats = new TreeMap<>();
    private Map<String, Integer> countryConfirmedStats = new TreeMap<>();
    private Triple<Integer, Integer, Integer> startStats = Triple.of(0, 0, 0);
    private String startStatsDay = "";

    public void setCountryStats(int daysConsideredNumber) {
        if (countryDeathsStats.isEmpty()) {
            countryDeathsStats = JsonDataParser.readTotalCountryStatistics(DataTypeEnum.DEATHS, daysConsideredNumber);
        }
        if (countryRecoveredStats.isEmpty()) {
            countryRecoveredStats = JsonDataParser.readTotalCountryStatistics(DataTypeEnum.CONFIRMED, daysConsideredNumber);
        }
        if (countryConfirmedStats.isEmpty()) {
            countryConfirmedStats = JsonDataParser.readTotalCountryStatistics(DataTypeEnum.RECOVERED, daysConsideredNumber);
        }

        String dataFromDay = wrapArray(countryDeathsStats.entrySet()).get(countryDeathsStats.size() - 2).getKey();
        startStatsDay = dataFromDay.replace('-', '/');

        List<Integer> confirmed = wrapArray(countryConfirmedStats.values());
        List<Integer> recovered = wrapArray(countryRecoveredStats.values());
        List<Integer> deaths = wrapArray(countryDeathsStats.values());

        startStats = Triple.of(
                confirmed.get(confirmed.size() - 2) - confirmed.get(confirmed.size() - 3),
                recovered.get(confirmed.size() - 2) - recovered.get(confirmed.size() - 3),
                deaths.get(confirmed.size() - 2) - deaths.get(confirmed.size() - 3));
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

    public Triple<Integer, Integer, Integer> getStartStats() {
        return startStats;
    }

    public String getStartStatsDay() {
        return startStatsDay;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }

    private <T> List<T> wrapArray(Collection<T> collection) {
        return new ArrayList<>(collection);
    }
}
