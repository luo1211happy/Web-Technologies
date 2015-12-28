package edu.usc.bob.forecast.model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import edu.usc.bob.forecast.ResultActivity;

/**
 * Created by BOB on 2015/11/21.
 */
public class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

    Activity activity;
    ProgressDialog dialog;
    public JSONWeatherTask(Activity activity) {
        this.activity = activity;
    }

    public Weather getWeather(String street, String city, String state, String degree, String data, String state_position_string) throws JSONException {
        Weather weather = new Weather();
        weather.setStreet(street);
        weather.setCity(city);
        weather.setState(state);
        weather.setDegree(degree);
        weather.setWeatherObject(data);
        int state_position = Integer.parseInt(state_position_string);
        weather.setState_position(state_position);
        return weather;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(activity);
        dialog.setMessage("Loading please wait");
        dialog.setTitle("Connecting server");
        dialog.show();
        dialog.setCancelable(false);

    }

    @Override
    protected Weather doInBackground(String... params) {
        Weather weather = new Weather();
        String data = ((new WeatherHttpClient()).getWeatherData(params[0], params[1], params[2], params[3]));

        try {
            weather = getWeather(params[0], params[1], params[2], params[3], data, params[4]);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return weather;

    }


    @Override
    protected void onPostExecute(Weather weather) {
        dialog.cancel();
        if(weather == null)
        {
            Toast.makeText(activity.getApplicationContext(),"Unable to get data from server,try it once again",Toast.LENGTH_LONG).show();
            return;
        }
        super.onPostExecute(weather);
        Intent intent = new Intent();
        intent.putExtra("myweather",weather);
        intent.setClass(activity, ResultActivity.class);
        activity.startActivity(intent);
    }
}
