package pl.lodz.p.mobi.covidapp.map.loader;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.osmdroid.bonuspack.kml.KmlFeature;
import org.osmdroid.bonuspack.kml.KmlLineString;
import org.osmdroid.bonuspack.kml.KmlPlacemark;
import org.osmdroid.bonuspack.kml.KmlPoint;
import org.osmdroid.bonuspack.kml.KmlPolygon;
import org.osmdroid.bonuspack.kml.KmlTrack;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;

import java.util.Map;

import pl.lodz.p.mobi.covidapp.R;

public class MyKmlStyler implements KmlFeature.Styler {
    MapView map;
    Map<String, Triplet<Integer, Double, Integer>> scrappedRegions;

    public MyKmlStyler(MapView map, Map<String, Triplet<Integer, Double, Integer>> scrappedRegions) {
        this.map = map;
        this.scrappedRegions = scrappedRegions;
    }

    @Override
    public void onFeature(Overlay overlay, KmlFeature kmlFeature) {
        System.out.println("Feature");
    }

    @Override
    public void onPoint(Marker marker, KmlPlacemark kmlPlacemark, KmlPoint kmlPoint) {
        System.out.println("Point");
    }

    @Override
    public void onLineString(Polyline polyline, KmlPlacemark kmlPlacemark, KmlLineString kmlLineString) {
        System.out.println("onLineString");
    }

    @Override
    public void onPolygon(Polygon polygon, KmlPlacemark kmlPlacemark, KmlPolygon kmlPolygon) {
        polygon.setInfoWindow(new BasicInfoWindow(org.osmdroid.bonuspack.R.layout.bonuspack_bubble, map));
        String region = kmlPlacemark.getExtendedData("nazwa");

        polygon.getFillPaint().setARGB(64, 255, 0, 0);
        polygon.getOutlinePaint().setStrokeWidth(1.35f);

        String key = scrappedRegions.keySet().stream().filter(x -> region.toLowerCase().contains(x.toLowerCase())).findFirst().orElse(null);

        if (key != null) {
            Triplet<Integer, Double, Integer> t = scrappedRegions.get(key);
            assert t != null;
            polygon.setTitle(region);
            String builder = "Liczba przypadków: " + t.first + "<br>" +
                    "Liczba na 10 tyś/m: " + t.second + "<br>" +
                    "Liczba zgonów: " + t.third + "<br>";
            polygon.setSubDescription(builder);
        }

        polygon.setOnClickListener(new Polygon.OnClickListener() {
            @Override
            public boolean onClick(Polygon polygon, MapView mapView, GeoPoint eventPos) {
                String subDescription = polygon.getSubDescription().replace("<br>", "\n");
                String title = polygon.getTitle();
                ((TextView)((Activity) mapView.getContext()).findViewById(R.id.countyTitleTextView)).setText(title);
                ((TextView)((Activity) mapView.getContext()).findViewById(R.id.countyInfoTextView)).setText(subDescription);
                ((Activity) mapView.getContext()).findViewById(R.id.countyInfoLayout).setVisibility(View.VISIBLE);
                polygon.showInfoWindow();
                return true;
            }
        });

    }

    @Override
    public void onTrack(Polyline polyline, KmlPlacemark kmlPlacemark, KmlTrack kmlTrack) {
        System.out.println("onTrack");
//        kmlTrack.applyDefaultStyling();
    }
}
