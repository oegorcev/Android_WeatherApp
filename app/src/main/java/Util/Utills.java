package Util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mr.Nobody43 on 23.09.2017.
 */

public class Utills {

    public static final String Base_URL ="http://api.openweathermap.org/data/2.5/weather?q=";
    public static final String ICON_URL ="http://api.openweathermap.org/img/w/";
    public static final String TOKEN = "&appid=7750e246f88347f2abd5bb7f29ca6d26";

    public static JSONObject getObect(String tagName, JSONObject jsonObject) throws JSONException
    {
        JSONObject jObj = jsonObject.getJSONObject(tagName);
        return  jObj;
    }

    public static String getString(String tagName, JSONObject jsonObject) throws  JSONException
    {
        return jsonObject.getString(tagName);
    }

    public static float getFloat(String tagName, JSONObject jsonObject) throws  JSONException
    {
        return (float) jsonObject.getDouble(tagName);
    }

    public static double getDouble (String tagName, JSONObject jsonObject) throws  JSONException
    {
        return (float) jsonObject.getDouble(tagName);
    }

    public static int getInt (String tagName, JSONObject jsonObject) throws  JSONException
    {
        return jsonObject.getInt(tagName);
    }
}
