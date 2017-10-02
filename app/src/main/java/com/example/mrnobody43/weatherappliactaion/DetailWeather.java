package com.example.mrnobody43.weatherappliactaion;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import Util.Utills;
import data.JSONWeatherParser;
import data.WeatherHttpClient;
import model.WeatherWeek;

public class DetailWeather extends AppCompatActivity {

    private ListView listViewWeek;
    private ArrayList<WeatherWeek> items;
    private String id = "484907";
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_weather);

        listViewWeek = (ListView) findViewById(R.id.listViewWeek);
        items = new ArrayList<WeatherWeek>();

        Intent intent = new Intent(DetailWeather.this.getIntent());

        id = intent.getStringExtra(Utills.CODE);

        renderWetherData(id);
    }

    public  void renderWetherData(String city) {
        DetailWeather.WeatherWeekTask weatherWeekTask = new DetailWeather.WeatherWeekTask();
        weatherWeekTask.execute(new String[]{city + "&units=metric"});
    }

    private class  WeatherWeekTask extends AsyncTask<String, Void, ArrayList<WeatherWeek>> {

        @Override
        protected ArrayList<WeatherWeek> doInBackground(String... params) {

            try {
                String data = ((new WeatherHttpClient()).getWeatherWeekData(params[0]));

                items = JSONWeatherParser.getWeatherWeek(data);

                return items;

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<WeatherWeek> weatherWeek) {

            super.onPostExecute(items);

            ArrayList<String> mas = new ArrayList<>();

            for(int i = 0; i < items.size(); ++i)
            {
                mas.add(items.get(i).getDate());
                DecimalFormat decimalFormat = new DecimalFormat("#.#");
                String tempFormat = decimalFormat.format(items.get(i).currentCondition.getTemperature());
                mas.set(i,mas.get(i) + " " + tempFormat + "°C" );

            }

            adapter = new ArrayAdapter<String>(DetailWeather.this, android.R.layout.simple_list_item_1, mas);

            listViewWeek.setAdapter(adapter);
        }
    }
}
