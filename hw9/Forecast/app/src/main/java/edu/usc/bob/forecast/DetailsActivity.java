package edu.usc.bob.forecast;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import edu.usc.bob.forecast.fragment.DaysFragment;
import edu.usc.bob.forecast.fragment.HoursFragment;
import edu.usc.bob.forecast.model.Weather;

public class DetailsActivity extends FragmentActivity {
    Button mHours;
    Button mDays;
    Weather weather;
    Context context;
    String offset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("DetailsActivity");
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        weather = (Weather) intent.getSerializableExtra("myweather");
        try {
            JSONObject jsonObject = new JSONObject(weather.getWeatherObject());
            offset = jsonObject.getString("offset");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        context = getApplicationContext();
        HoursFragment fragment = new HoursFragment();
        fragment.getTarget(weather,context,offset);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.detail_container, fragment);
        fragmentManager.popBackStack();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        TextView mTitle = (TextView)findViewById(R.id.more_detail);
        String[] mState_Code = getResources().getStringArray(R.array.state_codes);
        String state_code = mState_Code[weather.getState_position() - 1];
        String more_detail_string = "More Details in " + weather.getCity() + "," + state_code;
        mTitle.setText(more_detail_string);
        mHours = (Button) findViewById(R.id.hours);
        mDays = (Button) findViewById(R.id.days);
        mHours.setPressed(true);
        mHours.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mHours.setPressed(true);
                mDays.setPressed(false);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                HoursFragment fragment = new HoursFragment();
                fragment.getTarget(weather,context,offset);
                fragmentTransaction.replace(R.id.detail_container, fragment);
                fragmentTransaction.addToBackStack("mytaghours");
                Fragment prevFragment = fragmentManager.findFragmentByTag("mytagdays");
                if (prevFragment != null) {
                    fragmentTransaction.remove(prevFragment);
                }
                fragmentManager.popBackStack();//solve fragment overlap problem
                fragmentTransaction.commit();
                return true;
            }
        });
        mDays.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDays.setPressed(true);
                mHours.setPressed(false);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                DaysFragment fragment = new DaysFragment();
                fragment.getTarget(weather,context,offset);
                fragmentTransaction.add(R.id.detail_container, fragment);
                fragmentTransaction.addToBackStack("mytagdays");
                Fragment prevFragment = fragmentManager.findFragmentByTag("mytaghours");
                if (prevFragment != null) {
                    fragmentTransaction.remove(prevFragment);
                }
                fragmentManager.popBackStack();
                fragmentTransaction.commit();
                return true;
            }
        });

    }
}
