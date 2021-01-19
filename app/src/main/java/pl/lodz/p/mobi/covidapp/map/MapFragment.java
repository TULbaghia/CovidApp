package pl.lodz.p.mobi.covidapp.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;
import java.util.UUID;
import pl.lodz.p.mobi.covidapp.R;
import pl.lodz.p.mobi.covidapp.map.loader.LoadGovData;
import pl.lodz.p.mobi.covidapp.viewmodel.DataViewModel;

import static android.view.View.GONE;


public class MapFragment extends Fragment {
    private DataViewModel dataViewModel;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
        dataViewModel = new ViewModelProvider((ViewModelStoreOwner) this.getContext()).get(DataViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.countyInfoLayout).setVisibility(GONE);
        initMapView();
//        initWebView(view);
        initTextZone();
    }

    private void initMapView() {
        Configuration.getInstance().setUserAgentValue("Android-TUL-Mobi-Addax-CovidApp-" + UUID.randomUUID().toString());

        MapView map = (MapView) requireView().findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.ALWAYS);
        map.setMultiTouchControls(true);

        GeoPoint startPoint = new GeoPoint(51.9194, 19.1451);
        IMapController mapController = map.getController();
        mapController.setZoom(7.2);
        mapController.setCenter(startPoint);
        new LoadGovData(map, getResources(), dataViewModel).execute("");
    }

    private void loadGovData() {

    }

    private void initTextZone() {
        dataViewModel.getCountyStats().forEach((x, y) -> {
            System.out.println(x);
            Log.d("TEST", x);
        });
    }
}