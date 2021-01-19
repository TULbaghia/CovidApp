package pl.lodz.p.mobi.covidapp.viewmodel;

import androidx.lifecycle.ViewModel;

import org.osmdroid.views.overlay.Polygon;

import java.util.Map;
import java.util.TreeMap;
import pl.lodz.p.mobi.covidapp.json.data.DataTypeEnum;
import pl.lodz.p.mobi.covidapp.json.JsonDataParser;
import pl.lodz.p.mobi.covidapp.json.data.model.CountyModel;
import pl.lodz.p.mobi.covidapp.map.loader.Triplet;

public class DataViewModel extends ViewModel {
    private Map<String, Triplet<Integer, Double, Integer>> scrappedData;
    private Map<String, Integer> countryDeathsStats = new TreeMap<>();
    private Map<String, Integer> countryRecoveredStats = new TreeMap<>();
    private Map<String, Integer> countryConfirmedStats = new TreeMap<>();
    private Map<String, CountyModel> countyStats = new TreeMap<>();
    private Polygon polygon;

    public Polygon getPolygon() {
        return polygon;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    public void setCountryStats(int daysConsideredNumber) {
        if (countryDeathsStats.isEmpty()) {
            countryDeathsStats = JsonDataParser.readTotalCountryStatistics(DataTypeEnum.DEATHS, daysConsideredNumber);
        }
        if(countryRecoveredStats.isEmpty()) {
            countryRecoveredStats = JsonDataParser.readTotalCountryStatistics(DataTypeEnum.CONFIRMED, daysConsideredNumber);
        }
        if(countryConfirmedStats.isEmpty()) {
            countryConfirmedStats = JsonDataParser.readTotalCountryStatistics(DataTypeEnum.RECOVERED, daysConsideredNumber);
        }
    }

    public void setCountyStats() {
        countyStats = JsonDataParser.readCountyStatistics();
    }

    public void setScrappedData(Map<String, Triplet<Integer, Double, Integer>> sData) {
        scrappedData = sData;
    }
    public Map<String, Triplet<Integer, Double, Integer>> getScrappedData() {
        return scrappedData;
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
