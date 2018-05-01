package com.example.anwyr1.astronomicweatherapp.XmlUtils;

import android.util.Xml;

import com.example.anwyr1.astronomicweatherapp.Weather.CityUtil.Coord;
import com.example.anwyr1.astronomicweatherapp.Weather.CityUtil.Country;
import com.example.anwyr1.astronomicweatherapp.Weather.CityUtil.Sun;
import com.example.anwyr1.astronomicweatherapp.Weather.CurrentWeather;
import com.example.anwyr1.astronomicweatherapp.Weather.CurrentWeatherUtil.City;
import com.example.anwyr1.astronomicweatherapp.Weather.CurrentWeatherUtil.Clouds;
import com.example.anwyr1.astronomicweatherapp.Weather.CurrentWeatherUtil.Humidity;
import com.example.anwyr1.astronomicweatherapp.Weather.CurrentWeatherUtil.LastUpdate;
import com.example.anwyr1.astronomicweatherapp.Weather.CurrentWeatherUtil.Precipitation;
import com.example.anwyr1.astronomicweatherapp.Weather.CurrentWeatherUtil.Pressure;
import com.example.anwyr1.astronomicweatherapp.Weather.CurrentWeatherUtil.Temperature;
import com.example.anwyr1.astronomicweatherapp.Weather.CurrentWeatherUtil.Visibility;
import com.example.anwyr1.astronomicweatherapp.Weather.CurrentWeatherUtil.Weather;
import com.example.anwyr1.astronomicweatherapp.Weather.CurrentWeatherUtil.Wind;
import com.example.anwyr1.astronomicweatherapp.Weather.WindUtil.Direction;
import com.example.anwyr1.astronomicweatherapp.Weather.WindUtil.Gusts;
import com.example.anwyr1.astronomicweatherapp.Weather.WindUtil.Speed;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

public class ActualWeatherXmlParser extends OpenWeatherApiXmlParser {

    public CurrentWeather parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readCurrentWeather(parser);
        } finally {
            in.close();
        }
    }

    private CurrentWeather readCurrentWeather(XmlPullParser parser) throws XmlPullParserException, IOException {
        City city = null;
        Temperature temperature = null;
        Humidity humidity = null;
        Pressure pressure = null;
        Wind wind = null;
        Clouds clouds = null;
        Visibility visibility = null;
        Precipitation precipitation = null;
        Weather weather = null;
        LastUpdate lastUpdate = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            switch (name) {
                case "city":
                    city = readCity(parser);
                    break;
                case "temperature":
                    temperature = readTemperature(parser);
                    parser.nextTag();
                    break;
                case "humidity":
                    humidity = readHumidity(parser);
                    parser.nextTag();
                    break;
                case "pressure":
                    pressure = readPressure(parser);
                    parser.nextTag();
                    break;
                case "wind":
                    wind = readWind(parser);
                    break;
                case "clouds":
                    clouds = readClouds(parser);
                    parser.nextTag();
                    break;
                case "visibility":
                    visibility = readVisibility(parser);
                    parser.nextTag();
                    break;
                case "precipitation":
                    precipitation = readPrecipitation(parser);
                    parser.nextTag();
                    break;
                case "weather":
                    weather = readWeather(parser);
                    parser.nextTag();
                    break;
                case "lastupdate":
                    lastUpdate = readLastUpdate(parser);
                    parser.nextTag();
                    break;
            }
        }
        return new CurrentWeather(city, temperature, humidity, pressure, wind,
                clouds, visibility, precipitation,weather, lastUpdate);
    }

    private Weather readWeather(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "weather");
        String number = parser.getAttributeValue(null, "number");
        String value = parser.getAttributeValue(null, "value");
        String icon = parser.getAttributeValue(null, "icon");
        return new Weather(number, value, icon);
    }

    private LastUpdate readLastUpdate(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "lastupdate");
        String value = parser.getAttributeValue(null, "value");
        return new LastUpdate(value);
    }

    private Visibility readVisibility(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "visibility");
        String value = parser.getAttributeValue(null, "value");
        return new Visibility(value);
    }

    private Precipitation readPrecipitation(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "precipitation");
        String mode = parser.getAttributeValue(null, "mode");
        return new Precipitation(mode);
    }

    private Clouds readClouds(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "clouds");
        String value = parser.getAttributeValue(null, "value");
        String name = parser.getAttributeValue(null, "name");
        return new Clouds(value, name);
    }

    private Wind readWind(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "wind");
        Speed speed = null;
        Gusts gusts = null;
        Direction direction = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tagName = parser.getName();
            switch (tagName) {
                case "speed":
                    speed = readSpeed(parser);
                    parser.nextTag();
                    break;
                case "gusts":
                    gusts = new Gusts();
                    parser.nextTag();
                    break;
                case "direction":
                    direction = readDirection(parser);
                    parser.nextTag();
                    break;
            }
        }
        parser.require(XmlPullParser.END_TAG, ns, "wind");
        return new Wind(speed, gusts, direction);
    }

    private Speed readSpeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "speed");
        String value = parser.getAttributeValue(null, "value");
        String name = parser.getAttributeValue(null, "name");
        return new Speed(value, name);
    }

    private Direction readDirection(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "direction");
        String value = parser.getAttributeValue(null, "value");
        String code = parser.getAttributeValue(null, "code");
        String name = parser.getAttributeValue(null, "name");
        return new Direction(value, code, name);
    }

    // Parses the contents of an entry. If it encounters a title, summary, or link tag, hands them off
// to their respective "read" methods for processing. Otherwise, skips the tag.
    private City readCity(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "city");
        String id = null;
        String name = null;
        Coord coord = null;
        Country country = null;
        Sun sun = null;
        id = parser.getAttributeValue(null, "id");
        name = parser.getAttributeValue(null, "name");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String tagName = parser.getName();
            switch (tagName) {
                case "coord":
                    coord = readCoord(parser);
                    parser.nextTag();
                    break;
                case "country":
                    country = readCountry(parser);
                    break;
                case "sun":
                    sun = readSun(parser);
                    parser.nextTag();
                    break;
            }
        }
        return new City(id, name, coord, country, sun);
    }

    // Processes title tags in the feed.
    private Coord readCoord(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "coord");
        String lon = parser.getAttributeValue(null, "lon");
        String lat = parser.getAttributeValue(null, "lat");
        return new Coord(lon, lat);
    }

    private Country readCountry(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "country");
        String code = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "country");
        return new Country(code);
    }

}
