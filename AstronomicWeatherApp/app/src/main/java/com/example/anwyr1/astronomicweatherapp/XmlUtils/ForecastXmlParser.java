package com.example.anwyr1.astronomicweatherapp.XmlUtils;

import android.util.Xml;

import com.example.anwyr1.astronomicweatherapp.Forecast.ForecastData;
import com.example.anwyr1.astronomicweatherapp.Forecast.ForecastUtils.Clouds;
import com.example.anwyr1.astronomicweatherapp.Forecast.ForecastUtils.Time;
import com.example.anwyr1.astronomicweatherapp.Forecast.ForecastUtils.Wind;
import com.example.anwyr1.astronomicweatherapp.Forecast.Location;
import com.example.anwyr1.astronomicweatherapp.Forecast.ThreeHoursForecast;
import com.example.anwyr1.astronomicweatherapp.Weather.CityUtil.Sun;
import com.example.anwyr1.astronomicweatherapp.Weather.CurrentWeatherUtil.Humidity;
import com.example.anwyr1.astronomicweatherapp.Weather.CurrentWeatherUtil.Pressure;
import com.example.anwyr1.astronomicweatherapp.Weather.CurrentWeatherUtil.Temperature;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anwyr1 on 29/04/2018.
 */

public class ForecastXmlParser extends OpenWeatherApiXmlParser{

    public ForecastData parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readForecastData(parser);
        } finally {
            in.close();
        }
    }

    private ForecastData readForecastData(XmlPullParser parser) throws XmlPullParserException, IOException {
        Location location = null;
        Sun sun = null;
        List<ThreeHoursForecast> forecastList = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            switch (name) {
                case "location":
                    location = readLocation(parser);
                    parser.next();
                    break;
                case "credit":
                case "meta":
                    skip(parser);
                    break;
                case "sun":
                    sun = readSun(parser);
                    parser.nextTag();
                    break;
                case "forecast":
                    forecastList = readForecastList(parser);
                    break;
            }
        }
        return new ForecastData(location, sun, forecastList);
    }

    private List<ThreeHoursForecast> readForecastList(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "forecast");
        List<ThreeHoursForecast> threeHoursForecastList = new ArrayList<>();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tagName = parser.getName();
            if (tagName.equals("time")) {
                threeHoursForecastList.add(readThreeHoursForecast(parser));
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, "forecast");
        return threeHoursForecastList;
    }

    private ThreeHoursForecast readThreeHoursForecast(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "time");
        String timeFrom = parser.getAttributeValue(null, "from");
        String timeTo = parser.getAttributeValue(null, "to");
        Time time = new Time(timeFrom, timeTo);
        Wind wind = null;
        Temperature temperature = null;
        Pressure pressure = null;
        Humidity humidity = null;
        Clouds clouds = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            switch (name) {
                case "symbol":
                case "precipitation":
                    skip(parser);
                    break;
                case "windDirection":
                    wind = readWind(parser);
                    parser.nextTag();
                    break;
                case "temperature":
                    temperature = readTemperature(parser);
                    parser.nextTag();
                    break;
                case "pressure":
                    pressure = readPressure(parser);
                    parser.nextTag();
                    break;
                case "humidity":
                    humidity = readHumidity(parser);
                    parser.nextTag();
                    break;
                case "clouds":
                    clouds = readClouds(parser);
                    parser.nextTag();
                    break;
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, "time");
        return new ThreeHoursForecast(time, wind, temperature, pressure, humidity, clouds);
    }

    private Clouds readClouds(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "clouds");
        String value = parser.getAttributeValue(null, "value");
        String all = parser.getAttributeValue(null, "all");
        String unit = parser.getAttributeValue(null, "unit");
        return new Clouds(value, all, unit);
    }

    private Wind readWind(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "windDirection");
        String deg = parser.getAttributeValue(null, "deg");
        String directionName = parser.getAttributeValue(null, "name");
        parser.next();
        parser.next();
        parser.require(XmlPullParser.START_TAG, ns, "windSpeed");
        String mps = parser.getAttributeValue(null, "mps");
        String speedName = parser.getAttributeValue(null, "name");
        return new Wind(deg, directionName, mps, speedName);
    }

    private Location readLocation(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "location");
        parser.next();
        parser.require(XmlPullParser.START_TAG, ns, "name");
        String name = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "name");
        skipEmptyTag(parser);
        parser.require(XmlPullParser.START_TAG, ns, "country");
        String country = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "country");
        skipEmptyTag(parser);
        parser.require(XmlPullParser.START_TAG, ns, "location");
        String latitude = parser.getAttributeValue(null, "latitude");
        String longitude = parser.getAttributeValue(null, "longitude");
        parser.nextTag();
        parser.require(XmlPullParser.END_TAG, ns, "location");
        return new Location(name, country, latitude, longitude);
    }

}
