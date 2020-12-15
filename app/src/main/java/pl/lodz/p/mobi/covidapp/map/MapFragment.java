package pl.lodz.p.mobi.covidapp.map;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;

import java.util.Objects;
import java.util.UUID;

import pl.lodz.p.mobi.covidapp.R;
import pl.lodz.p.mobi.covidapp.map.dialogButtons.GreenButtonFragment;
import pl.lodz.p.mobi.covidapp.map.dialogButtons.OrangeButtonFragment;
import pl.lodz.p.mobi.covidapp.map.dialogButtons.RedButtonFragment;
import pl.lodz.p.mobi.covidapp.map.loader.LoadGovData;
import pl.lodz.p.mobi.covidapp.viewmodel.DataViewModel;


public class MapFragment extends Fragment {
    private DataViewModel dataViewModel;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
        dataViewModel = new ViewModelProvider(requireActivity()).get(DataViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initMapView();
//        initWebView(view);
        initZoneButtons();
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

        new LoadGovData(map, getResources()).execute("");
    }

//    @SuppressLint("SetJavaScriptEnabled")
//    private void initWebView(View view) {
//        WebView webView = (WebView) view.findViewById(R.id.webView);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.loadUrl("https://www.arcgis.com/apps/opsdashboard/index.html#/5dc92a9a7ff8490cbcd98bf8c5cc3ed0");
//    }

    private void initZoneButtons() {
        FragmentManager fm = getParentFragmentManager();

        requireView().findViewById(R.id.greenZoneButton).setOnClickListener(view1 -> {
            GreenButtonFragment dialogFragment = new GreenButtonFragment();
            dialogFragment.show(fm, "Sample Fragment");
        });

        requireView().findViewById(R.id.orangeZoneButton).setOnClickListener(view1 -> {
            OrangeButtonFragment dialogFragment = new OrangeButtonFragment();
            dialogFragment.show(fm, "Sample Fragment");
        });

        requireView().findViewById(R.id.redZoneButton).setOnClickListener(view1 -> {
            RedButtonFragment dialogFragment = new RedButtonFragment();
            dialogFragment.show(fm, "Sample Fragment");
        });
    }

    private void initTextZone() {
        dataViewModel.getCountyStats().forEach((x, y) -> {
            System.out.println(x);
            Log.d("TEST", x);
        });
    }
}