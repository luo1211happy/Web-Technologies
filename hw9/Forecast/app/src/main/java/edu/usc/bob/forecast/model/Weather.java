package edu.usc.bob.forecast.model;

import android.os.Parcelable;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by BOB on 2015/11/21.
 */
public class Weather implements Serializable {
    private String street;
    private String city;
    private String state;
    private int state_position;
    private String degree;
    private String weatherObject;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public int getState_position() {
        return state_position;
    }

    public void setState_position(int state_position) {
        this.state_position = state_position;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getWeatherObject() {
        return weatherObject;
    }

    public void setWeatherObject(String weatherObject) {
        this.weatherObject = weatherObject;
    }
}
