package pl.lodz.p.mobi.covidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.os.StrictMode;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import pl.lodz.p.mobi.covidapp.persistance.SQLiteHelper;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView mainNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainNavigation = findViewById(R.id.mainNavigation);
        NavController navController = Navigation.findNavController(this, R.id.changeableFragment);
        NavigationUI.setupWithNavController(mainNavigation, navController);


        // TODO: check if change is possible.
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
   }
}
