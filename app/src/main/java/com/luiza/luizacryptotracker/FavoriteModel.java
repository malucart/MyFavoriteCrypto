package com.luiza.luizacryptotracker;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("FavoriteModel")
public class FavoriteModel extends ParseObject {

    public static final String KEY_USER = "user";
    public static final String TAG = "FavoriteModel";
    public static final String KEY_NAME = "name";
    public static final String KEY_SYMBOL = "symbol";
    public static final String KEY_LOGOURL = "logoURL";
    public static final String KEY_PRICE = "price";
    public static final String KEY_ONEHOUR = "oneHour";
    public static final String KEY_TWENTYFOURHOUR = "twentyFourHour";
    public static final String KEY_ONEWEEK = "oneWeek";
    public static final String KEY_FAVSTATUS = "favStatus";

    /*
    public FavoriteModel() {
        super();
    }
    */

    // getters
    public ParseUser getUser() { return getParseUser(KEY_USER); }
    public String getName() { return getString(KEY_NAME); }
    public String getSymbol() { return getString(KEY_SYMBOL); }
    public String getLogoURL() { return getString(KEY_LOGOURL); }
    public Number getPrice() { return getNumber(String.valueOf(KEY_PRICE)); }
    public Number getOneHour() { return getNumber(String.valueOf(KEY_ONEHOUR)); }
    public Number getTwentyFourHour() { return getNumber(String.valueOf(KEY_TWENTYFOURHOUR)); }
    public Number getOneWeek() { return getNumber(String.valueOf(KEY_ONEWEEK)); }
    public Boolean getFavStatus() { return getBoolean(String.valueOf(KEY_FAVSTATUS)); }

    // setters
    public void setUser(ParseUser user) { put(KEY_USER, user); }
    public void setName(String name) { put(KEY_NAME, name); }
    public void setSymbol(String symbol) { put(KEY_SYMBOL, symbol); }
    public void setLogoURL(String logoURL) { put(KEY_LOGOURL, logoURL); }
    public void setPrice(Number price) { put(String.valueOf(KEY_PRICE), price); }
    public void setOneHour(Number oneHour) { put(String.valueOf(KEY_ONEHOUR), oneHour); }
    public void setTwentyFourHour(Number twentyFourHour) { put(String.valueOf(KEY_TWENTYFOURHOUR), twentyFourHour); }
    public void setOneWeek(Number oneWeek) { put(String.valueOf(KEY_ONEWEEK), oneWeek); }
    public void setFavStatus(Boolean favStatus) { put(String.valueOf(KEY_FAVSTATUS), favStatus);}
}
