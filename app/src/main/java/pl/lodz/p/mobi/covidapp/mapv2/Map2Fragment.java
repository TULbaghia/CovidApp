package pl.lodz.p.mobi.covidapp.mapv2;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.asksira.webviewsuite.WebViewSuite;

import pl.lodz.p.mobi.covidapp.R;

public class Map2Fragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map2, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initWebView();
    }

    private void initWebView() {
        WebViewSuite webView = requireView().findViewById(R.id.webViewSuite);
        webView.customizeClient(new WebViewSuite.WebViewSuiteCallback() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                view.loadUrl("javascript:(function() { " +
                        "var executeInt = setInterval(function() {" +
                        "   if(document.querySelector('[heading=\"Legend\"] [slot=\"control\"] button:last-child')) {" +
                        "      document.querySelector('[heading=\"Legend\"] [slot=\"control\"] button:last-child').click();" +
                        "      clearInterval(executeInt);" +
                        "  }" +
                        "}, 250);" +
                        "})()");
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
    }
}