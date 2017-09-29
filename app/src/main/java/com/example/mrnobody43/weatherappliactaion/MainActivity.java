package com.example.mrnobody43.weatherappliactaion;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import data.JSONWeatherParser;
import data.WeatherHttpClient;
import model.Weather;

public class MainActivity extends AppCompatActivity {

    private TextView cityName;
    private TextView temp;
    private ImageView iconView;
    private TextView description;
    private TextView humidity;
    private TextView pressure;
    private TextView wind;
    private TextView sunrise;
    private TextView sunset;
    private TextView updated;
    private Button changeCity;
    private  Button detailInformation;
    public static ArrayList<String> cityNameArray;
    private String id = "484907";


    Weather weather = new Weather();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = (TextView) findViewById(R.id.cityText);
        iconView = (ImageView) findViewById(R.id.thumbnailIcon);
        temp = (TextView) findViewById(R.id.textTemp);
        description = (TextView) findViewById(R.id.cloudText);
        humidity = (TextView) findViewById(R.id.humidText);
        pressure = (TextView) findViewById(R.id.pressureText);
        wind = (TextView) findViewById(R.id.windText);
        sunrise = (TextView) findViewById(R.id.riseText);
        sunset = (TextView) findViewById(R.id.setText);
        updated = (TextView) findViewById(R.id.updateText);
        changeCity = (Button) findViewById(R.id.change_city);
        detailInformation = (Button) findViewById(R.id.detail_information);

        cityNameArray = new ArrayList<String>(200000);

        Reader reader = new Reader();
        reader.execute();

        renderWetherData(id);

        changeCity.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, Search.class);
                startActivityForResult(intent, 1);
            }
        });

        detailInformation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, DetailWeather.class);
                intent.putExtra("code", id);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            id = data.getStringExtra("code");
            renderWetherData(id);
        }
    }

    public  void renderWetherData(String city) {
        WeatherTask weatherTask = new WeatherTask();
        weatherTask.execute(new String[]{city + "&units=metric"});
    }


    private class  WeatherTask extends AsyncTask<String, Void, Weather>{

        @Override
        protected Weather doInBackground(String... params) {

            try {
                String data = ((new WeatherHttpClient()).getWetherData(params[0]));

                weather = JSONWeatherParser.getWeatger(data);

                return weather;

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Weather weather) {

            super.onPostExecute(weather);

            DateFormat df = DateFormat.getTimeInstance();

            Date dSunrise = new Date(weather.place.getSunrise() * 1000);
            Date dSunset = new Date(weather.place.getSunset() * 1000);
            Date dUpdate = new Date(weather.place.getLastupdate() * 1000);
            SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss");

            f.setTimeZone(TimeZone.getTimeZone("GMT+3:00"));

            String sunriseDate = f.format(dSunrise);
            String sunsetDate = f.format(dSunset);
            String updateDate = f.format(dUpdate);


            DecimalFormat decimalFormat = new DecimalFormat("#.#");
            String tempFormat = decimalFormat.format(weather.currentCondition.getTemperature());

            cityName.setText(weather.place.getCity() + "," + weather.place.getCountry());
            temp.setText("" + tempFormat + "°C");
            humidity.setText("Humidity: " + weather.currentCondition.getHumidity()+ "%");
            pressure.setText("Pressure: " + weather.currentCondition.getPressure() + "hPa");
            wind.setText("Wind: " + weather.wind.getSpeed() + "mps");
            sunset.setText("Sunset: " + sunsetDate);
            sunrise.setText("Sunrise: " + sunriseDate);
            updated.setText("Last Updated: " + updateDate);
            description.setText("Condition: " + weather.currentCondition.getCondition() + " (" + weather.currentCondition.getDescription() + ")");
            int id = getResources().getIdentifier("com.example.mrnobody43.weatherappliactaion:drawable/" + "i" + weather.currentCondition.getIcon(), null, null);

            iconView.setImageResource(id);
        }
    }

    class Reader extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            changeCity.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            String file = "cities";
            int resId = getApplicationContext().getResources().getIdentifier(file, "raw", getApplicationContext().getPackageName());
            InputStream inputStream = getApplicationContext().getResources().openRawResource(resId);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream), 8192);
            try {
                String test;
                while (true) {
                    test = reader.readLine();
                    if (test == null)
                        break;
                    cityNameArray.add(test);

                }
                inputStream.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            changeCity.setVisibility(View.VISIBLE);
        }
    }

}
