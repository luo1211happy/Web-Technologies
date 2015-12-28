package edu.usc.bob.forecast.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.usc.bob.forecast.R;
import edu.usc.bob.forecast.model.Utility;
import edu.usc.bob.forecast.model.Weather;

public class DaysFragment extends Fragment {

    Weather weather;
    Context context;
    String offset;
    Utility utility;

    public void getTarget(Weather weather, Context context, String offset) {
        this.weather = weather;
        this.context = context;
        this.offset = offset;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_days, container, false);
        utility = new Utility();
        try {
            JSONObject jsonObject = new JSONObject(weather.getWeatherObject());
            JSONObject days = jsonObject.getJSONObject("daily");
            JSONArray data = days.getJSONArray("data");
            LinearLayout days_linear = (LinearLayout) view.findViewById(R.id.days_linear);
            for (int i = 1; i <= 7; i++) {
                JSONObject data_item = data.getJSONObject(i);
                LinearLayout days_item = new LinearLayout(context);
                LinearLayout.LayoutParams days_item_layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                days_item_layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                days_item.setOrientation(LinearLayout.VERTICAL);
                Resources res = getResources();
                TypedArray colors = res.obtainTypedArray(R.array.colors);
                int color = colors.getColor(i - 1, 0);
                days_item.setBackgroundColor(color);
                //inner LinearLayout
                LinearLayout days_content = new LinearLayout(context);
                LinearLayout.LayoutParams days_content_layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
                days_content_layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
                days_content_layoutParams.bottomMargin = 16;
                days_content.setOrientation(LinearLayout.HORIZONTAL);
                //specific contents
                TextView specific_time = new TextView(context);
                LinearLayout.LayoutParams specific_layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                specific_time.setTextColor(Color.parseColor("#000000"));
                specific_time.setTextSize(25);
                String time = utility.getTarget(data_item, "time", "SpecificDate", offset);
                specific_time.setText(time);
                specific_layoutParams.gravity = Gravity.LEFT;
                specific_layoutParams.weight = 11;
                specific_time.setLayoutParams(specific_layoutParams);

                ImageView weather_icon = new ImageView(context);
                LinearLayout.LayoutParams icon_layoutParams = new LinearLayout.LayoutParams(60, 60);
                icon_layoutParams.gravity = Gravity.RIGHT;
                utility.getImage(weather_icon, data_item.getString("icon"));
                icon_layoutParams.weight = 1;
                weather_icon.setLayoutParams(icon_layoutParams);
                days_content.addView(specific_time);
                days_content.addView(weather_icon);
                //Temperature
                TextView min_max_temp = new TextView(context);
                LinearLayout.LayoutParams temp_layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                temp_layoutParams.bottomMargin = 16;
                String min_temp = utility.getTarget(data_item, "temperatureMin", "Integer", offset);
                String max_temp = utility.getTarget(data_item, "temperatureMax", "Integer", offset);
                String min_max;
                if (weather.getDegree().equals("fahrenheit")) {
                    min_max = "Min:" + min_temp + (char) 0x2109 + "| Max:" + max_temp + (char) 0x2109;
                } else {
                    min_max = "Min:" + min_temp + (char) 0x2103 + "| Max:" + max_temp + (char) 0x2103;
                }
                min_max_temp.setLayoutParams(temp_layoutParams);
                min_max_temp.setText(min_max);
                min_max_temp.setTextColor(Color.parseColor("#000000"));
                min_max_temp.setTextSize(25);
                days_item.addView(days_content);
                days_item.addView(min_max_temp);
                days_linear.addView(days_item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }


}
