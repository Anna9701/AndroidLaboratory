package com.example.anwyr1.astronomicweatherapp.XmlUtils;

import com.example.anwyr1.astronomicweatherapp.Weather.CityUtil.Sun;
import com.example.anwyr1.astronomicweatherapp.Weather.CurrentWeatherUtil.Humidity;
import com.example.anwyr1.astronomicweatherapp.Weather.CurrentWeatherUtil.Pressure;
import com.example.anwyr1.astronomicweatherapp.Weather.CurrentWeatherUtil.Temperature;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by anwyr1 on 01/05/2018.
 */

public abstract class OpenWeatherApiXmlParser {
    // We don't use namespaces
    protected static final String ns = null;

    protected Temperature readTemperature(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "temperature");
        String value = parser.getAttributeValue(null, "value");
        String min = parser.getAttributeValue(null, "min");
        String max = parser.getAttributeValue(null, "max");
        String unit = parser.getAttributeValue(null, "unit");
        return new Temperature(value, min, max, unit);
    }

    protected Humidity readHumidity(XmlPullParser parser) throws XmlPullParserException, IOException {
        String value = parser.getAttributeValue(null, "value");
        String unit = parser.getAttributeValue(null, "unit");
        return new Humidity(value, unit);
    }

    protected Pressure readPressure(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "pressure");
        String value = parser.getAttributeValue(null, "value");
        String unit = parser.getAttributeValue(null, "unit");
        return new Pressure(value, unit);
    }

    protected Sun readSun(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "sun");
        String rise = parser.getAttributeValue(null, "rise");
        String set = parser.getAttributeValue(null, "set");
        return new Sun(rise, set);
    }

    protected void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    protected void skipEmptyTag(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.next();
        parser.next();
        parser.nextTag();
    }

    protected String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
}
