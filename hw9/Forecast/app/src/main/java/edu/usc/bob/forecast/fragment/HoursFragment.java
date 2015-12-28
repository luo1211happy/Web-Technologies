package edu.usc.bob.forecast.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import edu.usc.bob.forecast.R;
import edu.usc.bob.forecast.model.Utility;
import edu.usc.bob.forecast.model.Weather;


public class HoursFragment extends Fragment {

    Weather weather;
    Context context;
    String offset;
    JSONObject hourly;
    JSONArray data;
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
        View view = inflater.inflate(R.layout.fragment_hours, container, false);
        utility = new Utility();
        TextView mTemperature = (TextView) view.findViewById(R.id.temperature);
        if (weather.getDegree().equals("fahrenheit")) {
            mTemperature.setText("Temp(" + (char) 0x2109 + ")");
        } else {
            mTemperature.setText("Temp(" + (char) 0x2103 + ")");
        }
        try {
            JSONObject jsonObject = new JSONObject(weather.getWeatherObject());
            hourly = jsonObject.getJSONObject("hourly");
            data = hourly.getJSONArray("data");
            final TableLayout tableLayout = (TableLayout) view.findViewById(R.id.hours_table);
            int i;
            for (i = 0; i < 24; i++) {
                JSONObject data_item = data.getJSONObject(i);
                TableRow row = new TableRow(context);
                if (i % 2 == 0) {
                    row.setBackgroundResource(R.color.even_tablerow);
                } else {
                    row.setBackgroundResource(R.color.odds_tablerow);
                }
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams();
                layoutParams.topMargin = 10;
                row.setLayoutParams(layoutParams);
                //Time
                TextView textView = new TextView(context);
                String hours_time = utility.getTarget(data_item, "time", "TimeStamp", offset);
                textView.setText(hours_time);
                TableRow.LayoutParams layoutParams1 = new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
                layoutParams1.column = 1;
                textView.setTextSize(25);
                layoutParams1.gravity = Gravity.LEFT;
                textView.setPadding(0, 5, 0, 0);
                textView.setTextColor(Color.parseColor("#000000"));
                textView.setLayoutParams(layoutParams1);
                row.addView(textView);
                //Summary
                ImageView imageView = new ImageView(context);
                TableRow.LayoutParams layoutParams2 = new TableRow.LayoutParams(60, 60);
                layoutParams2.gravity = Gravity.CENTER;
                imageView.setLayoutParams(layoutParams2);
                utility.getImage(imageView, data_item.getString("icon"));
                row.addView(imageView);
                //Temperature
                TextView textView1 = new TextView(context);
                TableRow.LayoutParams layoutParams3 = new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
                layoutParams3.gravity = Gravity.RIGHT;
                textView1.setLayoutParams(layoutParams3);
                String temperature = utility.getTarget(data_item, "temperature", "Integer", offset);
                textView1.setText(temperature);
                textView1.setTextSize(25);
                textView1.setTextColor(Color.parseColor("#000000"));
                textView1.setPadding(0, 5, 20, 0);
                row.addView(textView1);
                tableLayout.addView(row);


            }

            final TableRow tableRow = new TableRow(context);
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams();
            layoutParams.topMargin = 10;
            tableRow.setLayoutParams(layoutParams);
            if (i % 2 == 0) {
                tableRow.setBackgroundResource(R.color.even_tablerow);
            } else {
                tableRow.setBackgroundResource(R.color.odds_tablerow);
            }
            TextView textView = new TextView(context);
            TableRow.LayoutParams layoutParams2 = new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
            layoutParams2.gravity = Gravity.CENTER;
            layoutParams2.column = 2;
            textView.setBackgroundResource(R.color.expand_button);
            textView.setLayoutParams(layoutParams2);
            textView.setText("+");
            textView.setTextSize(15);
            textView.setTextColor(Color.parseColor("#000000"));
            textView.setPadding(20, 10, 20, 10);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tableLayout.removeView(tableRow);
                    for (int j = 24; j < 48; j++) {
                        try {
                            JSONObject data_item = data.getJSONObject(j);
                            TableRow row = new TableRow(context);
                            if (j % 2 == 0) {
                                row.setBackgroundResource(R.color.even_tablerow);
                            } else {
                                row.setBackgroundResource(R.color.odds_tablerow);
                            }
                            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams();
                            layoutParams.topMargin = 10;
                            row.setLayoutParams(layoutParams);
                            //Time
                            TextView textView = new TextView(context);
                            String hours_time = utility.getTarget(data_item, "time", "TimeStamp", offset);
                            textView.setText(hours_time);
                            TableRow.LayoutParams layoutParams1 = new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams1.column = 1;
                            textView.setTextSize(25);
                            layoutParams1.gravity = Gravity.LEFT;
                            textView.setPadding(0, 5, 0, 0);
                            textView.setTextColor(Color.parseColor("#000000"));
                            textView.setLayoutParams(layoutParams1);
                            row.addView(textView);
                            //Summary
                            ImageView imageView = new ImageView(context);
                            TableRow.LayoutParams layoutParams2 = new TableRow.LayoutParams(60, 60);
                            layoutParams2.gravity = Gravity.CENTER;
                            imageView.setLayoutParams(layoutParams2);
                            utility.getImage(imageView, data_item.getString("icon"));
                            row.addView(imageView);
                            //Temperature
                            TextView textView1 = new TextView(context);
                            TableRow.LayoutParams layoutParams3 = new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
                            layoutParams3.gravity = Gravity.RIGHT;
                            textView1.setLayoutParams(layoutParams3);
                            String temperature = utility.getTarget(data_item, "temperature", "Integer", offset);
                            textView1.setText(temperature);
                            textView1.setTextSize(25);
                            textView1.setTextColor(Color.parseColor("#000000"));
                            textView1.setPadding(0, 5, 20, 0);
                            row.addView(textView1);
                            tableLayout.addView(row);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                }

            }
        });
            tableRow.addView(textView);
            tableLayout.addView(tableRow);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }


}
