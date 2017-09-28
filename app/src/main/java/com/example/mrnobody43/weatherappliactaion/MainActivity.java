package com.example.mrnobody43.weatherappliactaion;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;

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

        renderWetherData("Moscow");
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

                Log.v("Data: ", weather.place.getCity());

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

            String sunriseDate = df.format(new Date(weather.place.getSunrise()));
            String sunsetDate = df.format(new Date(weather.place.getSunset()));
            String updateDate = df.format(new Date(weather.place.getLastupdate()));

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
}
