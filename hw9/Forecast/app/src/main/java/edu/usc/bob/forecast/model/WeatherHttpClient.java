package edu.usc.bob.forecast.model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by BOB on 2015/11/21.
 */
public class WeatherHttpClient {
    private static String PHP_SERVER = "http://ec2-52-32-126-76.us-west-2.compute.amazonaws.com/hw8/hw8.php?";


    public String getWeatherData(String street,String city, String state, String degree) {
        HttpURLConnection con = null;
        InputStream is = null;
        street = street.replaceAll(" ","+");
        city = city.replaceAll(" ","+");
        state = state.replaceAll(" ","+");

        try {
            con = (HttpURLConnection) (new URL(PHP_SERVER +"street="+street+"&city="+city+"&state="+state+"&degree="+degree)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            // Let's read the response
            StringBuffer buffer = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = br.readLine()) != null)
                buffer.append(line + "\r\n");

            is.close();
            con.disconnect();
            return buffer.toString();
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (Throwable t) {
            }
            try {
                con.disconnect();
            } catch (Throwable t) {
            }
        }

        return null;

    }
}
