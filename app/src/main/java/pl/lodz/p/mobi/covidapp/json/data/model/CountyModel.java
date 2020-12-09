package pl.lodz.p.mobi.covidapp.json.data.model;

import org.json.JSONException;
import org.json.JSONObject;

public class CountyModel {
    private String county;
    private int confirmed;
    private int dead;
    private int healed;
    private int infected;
    private int hospitalized;
    private int quarantine;
    private int inspection;
    private int totalCitizens;

    public CountyModel(JSONObject attr) throws JSONException {
        this.setCounty(attr.getString("wojewodztwo"))
                .setConfirmed(attr.getInt("potwierdzone"))
                .setDead(attr.getInt("zmarli"))
                .setHealed(attr.getInt("wyleczeni"))
                .setInfected(attr.getInt("aktualnie_zarazeni"))
                .setHospitalized(attr.getInt("hospitalizacja"))
                .setQuarantine(attr.getInt("kwarantanna"))
                .setInspection(attr.getInt("nadzor"))
                .setTotalCitizens(attr.getInt("liczba_mieszkancow"));
    }

    public String getCounty() {
        return county;
    }

    public CountyModel setCounty(String county) {
        this.county = county;
        return this;
    }

    public int getConfirmed() {
        return confirmed;
    }

    public CountyModel setConfirmed(int confirmed) {
        this.confirmed = confirmed;
        return this;
    }

    public int getDead() {
        return dead;
    }

    public CountyModel setDead(int dead) {
        this.dead = dead;
        return this;
    }

    public int getHealed() {
        return healed;
    }

    public CountyModel setHealed(int healed) {
        this.healed = healed;
        return this;
    }

    public int getInfected() {
        return infected;
    }

    public CountyModel setInfected(int infected) {
        this.infected = infected;
        return this;
    }

    public int getHospitalized() {
        return hospitalized;
    }

    public CountyModel setHospitalized(int hospitalized) {
        this.hospitalized = hospitalized;
        return this;
    }

    public int getQuarantine() {
        return quarantine;
    }

    public CountyModel setQuarantine(int quarantine) {
        this.quarantine = quarantine;
        return this;
    }

    public int getInspection() {
        return inspection;
    }

    public CountyModel setInspection(int inspection) {
        this.inspection = inspection;
        return this;
    }

    public int getTotalCitizens() {
        return totalCitizens;
    }

    public CountyModel setTotalCitizens(int totalCitizens) {
        this.totalCitizens = totalCitizens;
        return this;
    }
}
