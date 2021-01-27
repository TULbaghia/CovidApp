package pl.lodz.p.mobi.covidapp.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import pl.lodz.p.mobi.covidapp.json.data.DataTypeEnum;

public class JsonDataParser {
    private static String readUrl(String urlString) throws IOException {
        String pageText;
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            pageText = reader.lines().collect(Collectors.joining("\n"));
        }
        return pageText;
    }

    public static Map<String, Integer> readTotalCountryStatistics(DataTypeEnum url, int daysConsideredNumber) {
        Map<String, Integer> parsedDataList = new LinkedHashMap<>();
        try {
            JSONArray json = new JSONArray(readUrl(url.getUrl()));
            for (int i = 0; i < daysConsideredNumber + 1; i++) {
                parsedDataList.put(
                        json.getJSONObject(json.length() - daysConsideredNumber + i).getString("Date").substring(5, 10),
                        json.getJSONObject(json.length() - daysConsideredNumber + i).getInt("Cases")
                );
            }
        } catch (JSONException | IOException e) {
            System.err.format("%s, %s", e, e.getMessage());
        }
        return parsedDataList;
    }
}
