package edu.usc.bob.forecast.model;

import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import edu.usc.bob.forecast.R;

/**
 * Created by BOB on 2015/11/21.
 */
public class Utility {
    public  String getConvertTime(long unix_timestamp, String offset) {
        Date date  = new Date(unix_timestamp * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa"); // the format of your date
        if(offset!=null)
        {
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"+offset)); // give a timezone reference for formating (see comment at the bottom
        }
        String formattedDate = sdf.format(date);
        System.out.println(formattedDate);
        return formattedDate;
    }
    public String getSpecificDate(long unix_timestamp, String offset){
        Date date  = new Date(unix_timestamp * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE,MMM dd"); // the format of your date
        if(offset!=null)
        {
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"+offset)); // give a timezone reference for formating (see comment at the bottom
        }
        String formattedDate = sdf.format(date);
        return formattedDate;
    }
    public String getTarget (JSONObject jsonObject, String target, String type, String offset) throws JSONException {
        String temp_string = "N.A.";
        if(jsonObject.has(target)&&jsonObject.getString(target)!=null)
        {
            String temp=null;
            switch (type)
            {
                case "Integer":
                    int temp_integer = jsonObject.getInt(target);
                    temp = Integer.toString(temp_integer);
                    break;
                case "Decimal":
                    double temp_double = jsonObject.getDouble(target);
                    BigDecimal b   =   new   BigDecimal(temp_double);
                    temp_double   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();
                    temp = Double.toString(temp_double);
                    break;
                case "TimeStamp":
                    long temp_long = jsonObject.getLong(target);
                    temp = getConvertTime(temp_long, offset);
                    break;
                case "SpecificDate":
                    long time_long = jsonObject.getLong(target);
                    temp = getSpecificDate(time_long,offset);
                    break;
                case "Percentage":
                    int temp_percentage = jsonObject.getInt(target);
                    temp = temp_percentage*100+"%";
                    break;
                default:
                    temp = jsonObject.getString(target);
            }
            if(temp!=null)
            {
                temp_string = temp;
            }
        }

        return  temp_string;
    }
    public void getImage(ImageView imageView,String icon)
    {
        switch (icon) {
            case "clear-day":
                imageView.setImageResource(R.drawable.clear);
                break;
            case "clear-night":
                imageView.setImageResource(R.drawable.clear_night);
                break;
            case "partly-cloudy-day":
                imageView.setImageResource(R.drawable.cloud_day);
                break;
            case "partly-cloudy-night":
                imageView.setImageResource(R.drawable.cloud_night);
                break;
            case "rain":
                imageView.setImageResource(R.drawable.rain);
                break;
            case "snow":
                imageView.setImageResource(R.drawable.rain);
                break;
            case "sleet":
                imageView.setImageResource(R.drawable.sleet);
                break;
            case "wind":
                imageView.setImageResource(R.drawable.wind);
                break;
            case "fog":
                imageView.setImageResource(R.drawable.fog);
                break;
            case "cloudy":
                imageView.setImageResource(R.drawable.cloudy);
                break;
        }

    }
}
