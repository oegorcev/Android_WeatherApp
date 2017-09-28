package data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Util.Utills;
import model.Place;
import model.Weather;

/**
 * Created by Mr.Nobody43 on 27.09.2017.
 */

public class JSONWeatherParser {

    public static Weather getWeatger(String data)
    {
        Weather weather = new Weather();

        try{
            JSONObject jsonObject = new JSONObject(data);

            Place place = new Place();

            //Coord information
            JSONObject coordObj = Utills.getObect("coord", jsonObject);
            place.setLat(Utills.getFloat("lat", coordObj));
            place.setLon(Utills.getFloat("lon", coordObj));

            //Sys information
            JSONObject sysObj = Utills.getObect("sys", jsonObject);
            place.setCountry(Utills.getString("country", sysObj));
            place.setLastupdate(Utills.getInt("dt", jsonObject));
            place.setSunrise(Utills.getInt("sunrise", sysObj));
            place.setSunset(Utills.getInt("sunset", sysObj));
            place.setCity(Utills.getString("name", jsonObject));
            weather.place = place;

            //Weather information
            JSONArray jsonArray =jsonObject.getJSONArray("weather");
            JSONObject jsonWeather = jsonArray.getJSONObject(0);
            weather.currentCondition.setWeatherId(Utills.getInt("id", jsonWeather));
            weather.currentCondition.setDescription(Utills.getString("description", jsonWeather));
            weather.currentCondition.setCondition(Utills.getString("main", jsonWeather));
            weather.currentCondition.setIcon(Utills.getString("icon", jsonWeather));

            //Temperature information
            JSONObject mainObj = Utills.getObect("main", jsonObject);
            weather.currentCondition.setHumidity(Utills.getInt("humidity", mainObj));
            weather.currentCondition.setPressure(Utills.getInt("pressure", mainObj));
            weather.currentCondition.setMinTemp(Utills.getFloat("temp_min", mainObj));
            weather.currentCondition.setMaxTemp(Utills.getFloat("temp_max", mainObj));
            weather.currentCondition.setTemperature(Utills.getDouble("temp",mainObj));

            //Wind information
            JSONObject windObj = Utills.getObect("wind", jsonObject);
            weather.wind.setSpeed(Utills.getFloat("speed", windObj));
            //weather.wind.setDeg(Utills.getFloat("deg", windObj));

            //Cloud information
            JSONObject cloudObj = Utills.getObect("clouds", jsonObject);
            weather.clouds.setPrecipitation(Utills.getInt("all", cloudObj));

            return weather;

        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        return null;
    }

}
