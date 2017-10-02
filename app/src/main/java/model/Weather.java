package model;

/**
 * Created by Mr.Nobody43 on 23.09.2017.
 */

public class Weather {

    public Place place;
    public CurrentCondition currentCondition = new CurrentCondition();
    public Wind wind = new Wind();
    public Clouds clouds = new Clouds();
}
