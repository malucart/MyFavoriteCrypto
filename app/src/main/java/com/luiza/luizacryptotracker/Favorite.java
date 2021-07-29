package com.luiza.luizacryptotracker;

import com.parse.ParseObject;
import com.parse.ParseUser;

public class Favorite extends ParseObject {

    public static String KEY_USER = "user";
    public String TAG = "Favorite";
    public String KEY_NAME = "name";
    public String KEY_SYMBOL = "symbol";
    public String KEY_LOGOURL = "logoURL";
    public String KEY_FAVSTATUS = "favStatus";
    public Number KEY_PRICE = Double.valueOf("price");
    public Number KEY_ONEHOUR = Double.valueOf("oneHour");
    public Number KEY_TWENTYFOURHOUR = Double.valueOf("twentyFourHour");
    public Number KEY_ONEWEEK = Double.valueOf("oneWeek");

    public Favorite() {

    }

    // getters
    public ParseUser getUser() { return getParseUser(KEY_USER); }
    public String getName() { return getString(KEY_NAME); }
    public String getSymbol() { return getString(KEY_SYMBOL); }
    public String getLogoURL() { return getString(KEY_LOGOURL); }
    public String getFavStatus() { return getString(KEY_FAVSTATUS); }
    public Number getPrice() { return getNumber(String.valueOf(KEY_PRICE)); }
    public Number getOneHour() { return getNumber(String.valueOf(KEY_ONEHOUR)); }
    public Number getTwentyFourHour() { return getNumber(String.valueOf(KEY_TWENTYFOURHOUR)); }
    public Number getOneWeek() { return getNumber(String.valueOf(KEY_ONEWEEK)); }

    // setters
    public void setUser(ParseUser user) { put(KEY_USER, user); }
    public void setName(String name) { put(KEY_NAME, name); }
    public void setSymbol(String symbol) { put(KEY_SYMBOL, symbol); }
    public void setLogoURL(String logoURL) { put(KEY_LOGOURL, logoURL); }
    public void setPrice(Number price) { put(String.valueOf(KEY_PRICE), price); }
    public void setOneHour(Number oneHour) { put(String.valueOf(KEY_ONEHOUR), oneHour); }
    public void setTwentyFourHour(Number twentyFourHour) { put(String.valueOf(KEY_TWENTYFOURHOUR), twentyFourHour); }
    public void setOneWeek(Number oneWeek) { put(String.valueOf(KEY_ONEWEEK), oneWeek); }
}
