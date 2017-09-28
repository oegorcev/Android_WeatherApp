package model;

/**
 * Created by Mr.Nobody43 on 23.09.2017.
 */

public class Weather {

    public Place place;
    public String iconData;
    public CurrentCondition currentCondition = new CurrentCondition();
    public Temperature temperature = new Temperature();
    public Wind wind = new Wind();
    public Snow snow = new Snow();
    public Clouds clouds = new Clouds();

}
