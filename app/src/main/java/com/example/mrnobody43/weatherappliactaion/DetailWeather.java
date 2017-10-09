package com.example.mrnobody43.weatherappliactaion;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import Util.Utills;
import data.DatabaseHelper;
import data.JSONWeatherParser;
import data.WeatherHttpClient;
import model.WeatherWeek;

public class DetailWeather extends AppCompatActivity {

    private ListView listViewWeek;
    private ArrayList<WeatherWeek> items;
    private String id = "484907";
    ArrayAdapter<String> adapter;
    private DatabaseHelper myDb;
    private String offlineData;
    private SQLiteDatabase db;


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_weather);

        listViewWeek = (ListView) findViewById(R.id.listViewWeek);
        items = new ArrayList<WeatherWeek>();

        Intent intent = new Intent(DetailWeather.this.getIntent());

        id = intent.getStringExtra(Utills.CODE);

        myDb = new DatabaseHelper(this);

        renderWetherData(id);
    }

    public  void renderWetherData(String city) {
        DetailWeather.WeatherWeekTask weatherWeekTask = new DetailWeather.WeatherWeekTask();

        if(isNetworkAvailable())
        {
            weatherWeekTask.execute(new String[]{city + "&units=metric"});
        }
        else
        {

            db = myDb.getReadableDatabase();

            Cursor c = db.query(DatabaseHelper.TABLE_NAME, null, null, null, null, null, null);

            if (c.moveToFirst())
            {

                boolean flag = true;
                while(true) {
                    if (c.isAfterLast()) break;

                    int idIndex = c.getColumnIndex(DatabaseHelper.ID);
                    int weekIndex = c.getColumnIndex(DatabaseHelper.JSON_WEEK);

                    offlineData = c.getString(weekIndex);
                    String bdId = c.getString(idIndex);
                    if (id.equals(bdId)){
                        weatherWeekTask.execute("db");
                        flag = false;
                        break;
                    } else c.moveToNext();
                }

                if(flag) Toast.makeText(this, "Weather for this city is empty :(", Toast.LENGTH_SHORT).show();

            } else
                Toast.makeText(this, "Need internet connection", Toast.LENGTH_SHORT).show();
            c.close();

        }



    }

    private class  WeatherWeekTask extends AsyncTask<String, Void, ArrayList<WeatherWeek>> {

        @Override
        protected ArrayList<WeatherWeek> doInBackground(String... params) {

            if(params[0] == "db")
            {

                if(!(offlineData.isEmpty())) items = JSONWeatherParser.getWeatherWeek(offlineData);

                return items;
            }
            else {

                try {
                    String data = ((new WeatherHttpClient()).getWeatherWeekData(params[0]));


                    items = JSONWeatherParser.getWeatherWeek(data);

                    return items;

                } catch (IOException e) {
                    e.printStackTrace();
                }
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
