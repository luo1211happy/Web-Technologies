package edu.usc.bob.forecast;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import org.json.JSONException;
import org.json.JSONObject;

import edu.usc.bob.forecast.model.JSONWeatherTask;
import edu.usc.bob.forecast.model.Weather;
import edu.usc.bob.forecast.model.WeatherHttpClient;

public class MainActivity extends AppCompatActivity {

    Spinner mSpinner;
    EditText mStreet;
    EditText mCity;
    Button mSearch;
    Button mClear;
    Button mAbout;
    ImageButton mForecast;
    TextSwitcher mTextSwitcher;
    String street;
    String city;
    String state;
    String degree;
    int state_position;

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide the title bar
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        // initialize the spinner
        mSpinner = (Spinner) findViewById(R.id.state);
        //build up data source
        String[] mItems = getResources().getStringArray(R.array.states_spinner);
        // establish adapter to bundle data
        ArrayAdapter<String> _Adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, mItems);
        //bundle adapter
        mSpinner.setAdapter(_Adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                validation();
                state_position = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        degree = "fahrenheit";
        mStreet = (EditText) findViewById(R.id.street);
        mCity = (EditText) findViewById(R.id.city);
        mSearch = (Button) findViewById(R.id.search_button);
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validation())
                {
//                    Intent intent = new Intent();
//                    intent.setClass(MainActivity.this, ResultActivity.class);
//                    MainActivity.this.startActivity(intent);
                    Activity activity = MainActivity.this;
                    street = mStreet.getText().toString();
                    city = mCity.getText().toString();
                    state = mSpinner.getSelectedItem().toString();
                    JSONWeatherTask task = new JSONWeatherTask(activity);
                    String state_position_string = Integer.toString(state_position);
                    task.execute(new String[]{street, city, state, degree,state_position_string});
                }
            }
        });
        mClear = (Button) findViewById(R.id.clear_button);
        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStreet.setText("");
                mCity.setText("");
                mSpinner.setSelection(0, true);
                RadioButton mFahrenheit= (RadioButton)findViewById(R.id.fahrenheit);
                mFahrenheit.setChecked(true);
                validation();

            }
        });
        mTextSwitcher = (TextSwitcher) findViewById(R.id.text_validation);
        mTextSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView tv = new TextView(MainActivity.this);
                tv.setTextSize(30);
                tv.setTextColor(Color.RED);
                tv.setGravity(Gravity.CENTER_HORIZONTAL);
                return tv;
            }
        });
        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
        mTextSwitcher.setInAnimation(in);
        mTextSwitcher.setOutAnimation(out);
        validation();
        mStreet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                validation();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validation();
            }

            @Override
            public void afterTextChanged(Editable s) {
                validation();
            }
        });
        mCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                validation();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validation();
            }

            @Override
            public void afterTextChanged(Editable s) {
                validation();
            }
        });
        //initialize all the data;
        mAbout = (Button)findViewById(R.id.about_button);
        mAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AboutActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        mForecast = (ImageButton)findViewById(R.id.forward_link);
        mForecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url="http://forecast.io";
                Uri uriUrl = Uri.parse(url);
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(launchBrowser);
            }
        });

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.fahrenheit:
                if (checked)
                    // choose fahrenheit
                    degree = "fahrenheit";
                break;
            case R.id.celsius:
                if (checked)
                    // choose celsius
                    degree = "celsius";
                break;
        }
    }

    public boolean validation() {
        String temp_street = mStreet.getText().toString().trim();
        String temp_city = mCity.getText().toString().trim();
        String temp_state = mSpinner.getSelectedItem().toString();
        if (temp_street.isEmpty()) {
            mTextSwitcher.setText("Please enter a Street");
            return false;
        } else if (temp_city.isEmpty()) {
            mTextSwitcher.setText("Please enter a City");
            return false;
        } else if (temp_state.equals("Select State")) {
            mTextSwitcher.setText("Please select a State");
            return false;
        }
        else {
            mTextSwitcher.setText("");
        }
        return true;
    }
//    private boolean isValCity(String temp_city) {
//        String CITY_PATTERN = "^[A-Za-z\\s]+$";
//        Pattern pattern = Pattern.compile(CITY_PATTERN);
//        Matcher matcher = pattern.matcher(temp_city);
//        return matcher.matches();
//    }

}
