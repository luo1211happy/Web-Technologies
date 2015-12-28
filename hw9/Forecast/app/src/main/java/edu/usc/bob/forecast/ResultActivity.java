package edu.usc.bob.forecast;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.usc.bob.forecast.model.Utility;
import edu.usc.bob.forecast.model.Weather;

public class ResultActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    ShareDialog shareDialog;
    ImageButton share;
    Button map;
    Button detail;
    ImageSwitcher mImageSwitcher;
    Weather weather;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("ResultActivity");
        setContentView(R.layout.activity_result);
        FacebookSdk.sdkInitialize(getApplicationContext());
        // Initialize the SDK before executing any other operations,
        // especially, if you're using Facebook UI elements.
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        share = (ImageButton) findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         try {
                                             JSONObject jsonObject = new JSONObject(weather.getWeatherObject());
                                             JSONObject currently = jsonObject.getJSONObject("currently");
                                             String[] mState_Code = getResources().getStringArray(R.array.state_codes);
                                             String state_code = mState_Code[weather.getState_position() - 1];
                                             String summary = currently.getString("summary");
                                             String temperature;
                                             if (weather.getDegree().equals("fahrenheit")) {
                                                 int current_fahrenheit = currently.getInt("temperature");
                                                 temperature = Integer.toString(current_fahrenheit);
                                                 temperature = temperature + "\u2109";
                                             } else {
                                                 int current_celsius = currently.getInt("temperature");
                                                 temperature = Integer.toString(current_celsius);
                                                 temperature = temperature + "\u2103";
                                             }
                                             String icon = currently.getString("icon");
                                             String icon_address = null;
                                             switch (icon) {
                                                 case "clear-day":
                                                     icon_address = "http://ec2-52-32-126-76.us-west-2.compute.amazonaws.com/hw8/images/clear.png";
                                                     break;
                                                 case "clear-night":
                                                     icon_address = "http://ec2-52-32-126-76.us-west-2.compute.amazonaws.com/hw8/images/clear_night.png";
                                                     break;
                                                 case "partly-cloudy-day":
                                                     icon_address = "http://ec2-52-32-126-76.us-west-2.compute.amazonaws.com/hw8/images/cloud_day.png";
                                                     break;
                                                 case "partly-cloudy-night":
                                                     icon_address = "http://ec2-52-32-126-76.us-west-2.compute.amazonaws.com/hw8/images/cloud_night.png";
                                                     break;
                                                 default:
                                                     icon_address = "http://ec2-52-32-126-76.us-west-2.compute.amazonaws.com/hw8/images/" + icon + ".png";

                                             }
                                             ShareLinkContent linkContent = new ShareLinkContent.Builder()
                                                     .setContentTitle("Current Weather in " + weather.getCity() + "," + state_code)
                                                     .setContentDescription(summary + "," + temperature)
                                                     .setContentUrl(Uri.parse("http://forecast.io"))
                                                     .setImageUrl(Uri.parse(icon_address))
                                                     .build();

                                             shareDialog.show(linkContent);
                                             shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                                                 @Override
                                                 public void onSuccess(Sharer.Result result) {
                                                 }

                                                 @Override
                                                 public void onCancel() {
                                                 }

                                                 @Override
                                                 public void onError(FacebookException e) {
                                                 }
                                             }, 111);
                                         } catch (JSONException e) {
                                             e.printStackTrace();
                                         }
                                     }
                                 }
        );
        map = (Button) findViewById(R.id.map);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("myweather", weather);
                intent.setClass(ResultActivity.this, MapActivity.class);
                ResultActivity.this.startActivity(intent);
            }
        });
        mImageSwitcher = (ImageSwitcher) findViewById(R.id.current_weather);
        mImageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView iv = new ImageView(ResultActivity.this);
                return iv;

            }
        });

        mImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,

                android.R.anim.fade_in));

        mImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,

                android.R.anim.fade_out));

        Intent intent = getIntent();
        weather = (Weather) intent.getSerializableExtra("myweather");
        try {
            JSONObject jsonObject = new JSONObject(weather.getWeatherObject());
            JSONObject currently = jsonObject.getJSONObject("currently");
            String icon = currently.getString("icon");
            switch (icon) {
                case "clear-day":
                    mImageSwitcher.setImageResource(R.drawable.clear);
                    break;
                case "clear-night":
                    mImageSwitcher.setImageResource(R.drawable.clear_night);
                    break;
                case "partly-cloudy-day":
                    mImageSwitcher.setImageResource(R.drawable.cloud_day);
                    break;
                case "partly-cloudy-night":
                    mImageSwitcher.setImageResource(R.drawable.cloud_night);
                    break;
                case "rain":
                    mImageSwitcher.setImageResource(R.drawable.rain);
                    break;
                case "snow":
                    mImageSwitcher.setImageResource(R.drawable.rain);
                    break;
                case "sleet":
                    mImageSwitcher.setImageResource(R.drawable.sleet);
                    break;
                case "wind":
                    mImageSwitcher.setImageResource(R.drawable.wind);
                    break;
                case "fog":
                    mImageSwitcher.setImageResource(R.drawable.fog);
                    break;
                case "cloudy":
                    mImageSwitcher.setImageResource(R.drawable.cloudy);
                    break;
            }
            TextView mSummary = (TextView) findViewById(R.id.summary);
            String[] mState_Code = getResources().getStringArray(R.array.state_codes);
            String state_code = mState_Code[weather.getState_position() - 1];
            String summary = currently.getString("summary") + " in " + weather.getCity() + "," + state_code;
            if (currently.getString("summary") == null) {
                mSummary.setText("N.A.");
            } else {
                mSummary.setText(summary);
            }
            TextView mTemperature = (TextView) findViewById(R.id.temperature);
            TextView mDegree = (TextView) findViewById(R.id.degree);
            String temperature = currently.getString("temperature");
            if (temperature == null) {
                mTemperature.setText("N.A.");
            } else {
                if (weather.getDegree().equals("fahrenheit")) {
                    int current_fahrenheit = currently.getInt("temperature");
                    temperature = Integer.toString(current_fahrenheit);
                    mTemperature.setText(temperature);
                    mDegree.setText("\u2109");
                } else {
                    int current_celsius = currently.getInt("temperature");
                    temperature = Integer.toString(current_celsius);
                    mTemperature.setText(temperature);
                    mDegree.setText(" \u2103");
                }
            }
            TextView mMin_Max_Temp = (TextView) findViewById(R.id.min_max_temp);
            JSONObject daily = jsonObject.getJSONObject("daily");
            JSONArray data = daily.getJSONArray("data");
            JSONObject dataJSONObject = data.getJSONObject(0);
            String min_temp = dataJSONObject.getString("temperatureMin");
            String max_temp = dataJSONObject.getString("temperatureMax");
            if (min_temp == null) {
                min_temp = "L: N.A.";
            } else {
                int current_min_temp = dataJSONObject.getInt("temperatureMin");
                min_temp = "L: " + Integer.toString(current_min_temp);
            }
            if (max_temp == null) {
                max_temp = "H: N.A.";
            } else {
                int current_max_temp = dataJSONObject.getInt("temperatureMax");
                max_temp = "H: " + Integer.toString(current_max_temp);
            }
            mMin_Max_Temp.setText(min_temp + " | " + max_temp);
            TextView mPrecipitation = (TextView) findViewById(R.id.precipitation);
            String pre_display;
            if (currently.getString("precipIntensity") == null) {
                pre_display = "N.A.";
                mPrecipitation.setText(pre_display);
            } else {
                double precipitation;
                if (currently.getString("precipIntensity").equals("0")) {
                    precipitation = 0.0;
                } else {
                    precipitation = currently.getDouble("precipIntensity");
                }
                if (precipitation >= 0 && precipitation < 0.002) {
                    pre_display = "None";
                    mPrecipitation.setText(pre_display);
                }
                if (precipitation >= 0.002 && precipitation < 0.017) {
                    pre_display = "Very Light";
                    mPrecipitation.setText(pre_display);
                }
                if (precipitation >= 0.017 && precipitation < 0.1) {
                    pre_display = "Light";
                    mPrecipitation.setText(pre_display);
                }
                if (precipitation >= 0.1 && precipitation < 0.4) {
                    pre_display = "Moderate";
                    mPrecipitation.setText(pre_display);
                }
                if (precipitation >= 0.4) {
                    pre_display = "Heavy";
                    mPrecipitation.setText(pre_display);
                }

            }
            TextView mChance_of_Rain = (TextView) findViewById(R.id.chance_of_rain);
            if (currently.getString("precipProbability") == null) {
                mChance_of_Rain.setText("N.A.");
            } else {
                int chance_of_rain = currently.getInt("precipProbability");
                mChance_of_Rain.setText(chance_of_rain * 100 + "%");
            }
            TextView mWind_Speed = (TextView) findViewById(R.id.wind_speed);
            TextView mDew_Point = (TextView) findViewById(R.id.dew_point);
            TextView mHumidity = (TextView) findViewById(R.id.humidity);
            TextView mVisibility = (TextView) findViewById(R.id.visibility);
            TextView mSunrise = (TextView) findViewById(R.id.sunrise);
            TextView mSunset = (TextView) findViewById(R.id.sunset);
            Utility utility = new Utility();
            String wind_string = utility.getTarget(currently, "windSpeed", "Decimal", jsonObject.getString("offset"));
            String dew_string = utility.getTarget(currently, "dewPoint", "Decimal", jsonObject.getString("offset"));
            String humidity_string = utility.getTarget(currently, "humidity", "Percentage", jsonObject.getString("offset"));
            String visibility_string = utility.getTarget(currently, "visibility", "Decimal", jsonObject.getString("offset"));
            String sunrise_string = utility.getTarget(dataJSONObject, "sunriseTime", "TimeStamp", jsonObject.getString("offset"));
            String sunset_string = utility.getTarget(dataJSONObject, "sunsetTime", "TimeStamp", jsonObject.getString("offset"));
            if (weather.getDegree().equals("fahrenheit")) {
                mWind_Speed.setText(wind_string + " mph");
                mDew_Point.setText(dew_string + (char) 0x2109);
                mVisibility.setText(visibility_string + " mi");
            } else {
                mWind_Speed.setText(wind_string + " m/s");
                mDew_Point.setText(dew_string + (char) 0x2103);
                mVisibility.setText(visibility_string + " km");

            }
            mHumidity.setText(humidity_string);
            mSunrise.setText(sunrise_string);
            mSunset.setText(sunset_string);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        detail = (Button) findViewById(R.id.details);
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("myweather", weather);
                intent.setClass(ResultActivity.this, DetailsActivity.class);
                ResultActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        //use the requestCode to selfdefine
        if (requestCode == 111) {
            Toast.makeText(getApplicationContext(), "Facebook Post Successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Posted Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}
