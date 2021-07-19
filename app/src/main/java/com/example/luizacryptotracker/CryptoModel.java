package com.example.luizacryptotracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class CryptoModel {

    private int id;

    private String name;
    private String symbol;
    private int price;
    private String percent_change_1h;
    private String percent_change_24h;
    private String percent_change_7d;

    // Getters
    public int getId() { return this.id; }

    public String getName() { return this.name; }
    public String getSymbol() { return this.symbol; }
    public int getPrice() { return this.price; }
    public String getPercent_change_1h() { return this.percent_change_1h; }
    public String getPercent_change_24h() { return this.percent_change_24h; }
    public String getPercent_change_7d() { return this.percent_change_7d; }

    // Setters
    public void setId(int newId) { this.id = newId; }

    public void setName(String newName) { this.name = newName; }
    public void setSymbol(String newSymbol) { this.symbol = newSymbol; }

    public void setPrice(int newPrice) { this.price = newPrice; }
    public void setPercent_change_1h(String newPercent_change_1h) { this.percent_change_1h = newPercent_change_1h; }
    public void setPercent_change_24h(String newPercent_change_24h) { this.percent_change_24h = newPercent_change_24h; }
    public void setPercent_change_7d(String newPercent_change_7d) { this.percent_change_7d = newPercent_change_7d; }

    public CryptoModel(JSONObject jsonObject) throws JSONException {

        id = jsonObject.getInt(String.valueOf(id));
        name = jsonObject.getString(String.valueOf(R.string.name));
        symbol = jsonObject.getString(String.valueOf(symbol));
        price = jsonObject.getInt(String.valueOf(price));
        percent_change_1h = jsonObject.getString(String.valueOf(percent_change_1h));
        percent_change_24h = jsonObject.getString(String.valueOf(percent_change_24h));
        percent_change_7d = jsonObject.getString(String.valueOf(percent_change_7d));
    }

    /*public static List<CryptoModel> fromJsonArray(JSONArray cryptoJsonArray) throws URISyntaxException, JSONException {
        List<CryptoModel> crypto = new ArrayList<>();
        for (int i = 0; i < cryptoJsonArray.length(); i++) {
            crypto.add(new CryptoModel(cryptoJsonArray.getJSONObject(i)));
        }
        return crypto;
    }*/
}
