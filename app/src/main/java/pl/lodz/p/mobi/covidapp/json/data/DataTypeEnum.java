package pl.lodz.p.mobi.covidapp.json.data;

public enum DataTypeEnum {

    DEATHS("https://api.covid19api.com/dayone/country/poland/status/deaths/live"),
    CONFIRMED("https://api.covid19api.com/dayone/country/poland/status/confirmed/live"),
    RECOVERED("https://api.covid19api.com/dayone/country/poland/status/recovered/live");

    private final String url;

    DataTypeEnum(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
