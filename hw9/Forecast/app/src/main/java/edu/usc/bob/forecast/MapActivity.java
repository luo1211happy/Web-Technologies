package edu.usc.bob.forecast;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.hamweather.aeris.communication.AerisEngine;

import edu.usc.bob.forecast.fragment.MapFragment;
import edu.usc.bob.forecast.model.Weather;

public class MapActivity extends FragmentActivity {

    Weather weather;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Intent intent = getIntent();
        weather = (Weather) intent.getSerializableExtra("myweather");
        context = MapActivity.this;
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MapFragment fragment = new MapFragment();
        fragment.getWeather(weather,context);
        fragmentTransaction.add(R.id.map_container, fragment);
        fragmentTransaction.commit();

    }
}
