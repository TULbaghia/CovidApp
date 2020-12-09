package pl.lodz.p.mobi.covidapp.map;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import pl.lodz.p.mobi.covidapp.R;
import pl.lodz.p.mobi.covidapp.viewmodel.DataViewModel;


public class MapFragment extends Fragment {
    private DataViewModel dataViewModel;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataViewModel = new ViewModelProvider(getActivity()).get(DataViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initWebView(view);
        initZoneButtons(view);
        initTextZone(view);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView(View view) {
        WebView webView = (WebView) view.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://www.arcgis.com/apps/opsdashboard/index.html#/5dc92a9a7ff8490cbcd98bf8c5cc3ed0");
    }

    private void initZoneButtons(View view) {
        FragmentManager fm = getParentFragmentManager();

        view.findViewById(R.id.greenZoneButton).setOnClickListener(view1 -> {
            GreenButtonFragment dialogFragment = new GreenButtonFragment();
            dialogFragment.show(fm, "Sample Fragment");
        });

        view.findViewById(R.id.orangeZoneButton).setOnClickListener(view1 -> {
            OrangeButtonFragment dialogFragment = new OrangeButtonFragment();
            dialogFragment.show(fm, "Sample Fragment");
        });

        view.findViewById(R.id.redZoneButton).setOnClickListener(view1 -> {
            RedButtonFragment dialogFragment = new RedButtonFragment();
            dialogFragment.show(fm, "Sample Fragment");
        });
    }

    private void initTextZone(View view) {
        dataViewModel.getCountyStats().forEach((x, y) -> {
            System.out.println(x);
            Log.d("TEST", x);
        });
    }
}