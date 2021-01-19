package pl.lodz.p.mobi.covidapp.map.loader;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.AsyncTask;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import pl.lodz.p.mobi.covidapp.viewmodel.DataViewModel;

public class LoadGovData extends AsyncTask<String, Integer, String> {
    private Map<String, Triplet<Integer, Double, Integer>> scrappedData;
    @SuppressLint("StaticFieldLeak")
    private MapView map;
    private Resources resources;
    private DataViewModel dataViewModel;

    public LoadGovData(MapView map, Resources resources, DataViewModel dataViewModel) {
        this.map = map;
        this.resources = resources;
        this.dataViewModel = dataViewModel;
    }

    private Map<String, Triplet<Integer, Double, Integer>> scrapper() {
        Map<String, Triplet<Integer, Double, Integer>> regions = new HashMap<>();
        try {
            Document doc = Jsoup.connect("https://www.gov.pl/web/koronawirus/mapa-zarazen-koronawirusem-sars-cov-2-powiaty").get();

            Elements elements = doc.select(".file-download[href]");

            String csvLink = elements.stream()
                    .map(x -> x.attr("href"))
                    .filter(x -> x.contains("https://www.gov.pl/attachment"))
                    .findFirst().orElse(null);

            InputStream is = new URL(csvLink).openStream();

            Reader text = new BufferedReader(new InputStreamReader(is, "Windows-1250"));

            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader().parse(text);
            records.iterator().next();

            records.forEach(x -> regions.put(x.get(1), new Triplet<>(
                    Integer.parseInt(x.get(2)),
                    Double.parseDouble(x.get(3).replace(',', '.')),
                    Integer.parseInt(x.get(4)))));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return regions;
    }

    @Override
    protected String doInBackground(String... strings) {
        if (dataViewModel.getScrappedData() == null) {
            scrappedData = scrapper();
            dataViewModel.setScrappedData(scrappedData);
        } else {
            scrappedData = dataViewModel.getScrappedData();
        }
        initOnMap();
        return "";
    }

    public void initOnMap() {
        MyKmlStyler myKmlStyler = new MyKmlStyler(map, scrappedData, dataViewModel);

        KmlDocument kmlDocument = new KmlDocument();
        try {
            InputStream is = resources.getAssets().open("map/powiaty-min.geojson");
            String text = new BufferedReader(
                    new InputStreamReader(is, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));

            kmlDocument.parseGeoJSON(text);
            FolderOverlay kmlOverlay = (FolderOverlay) kmlDocument.mKmlRoot.buildOverlay(map, null, myKmlStyler, kmlDocument);

            map.getOverlays().add(kmlOverlay);
            map.invalidate();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
