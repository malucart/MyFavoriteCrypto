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

    private double price;
    private double percent_change_1h;
    private double percent_change_24h;
    private double percent_change_7d;

    // Getters
    public int getId() { return this.id; }

    public String getName() { return this.name; }
    public String getSymbol() { return this.symbol; }

    public double getPrice() { return this.price; }
    public double getPercent_change_1h() { return this.percent_change_1h; }
    public double getPercent_change_24h() { return this.percent_change_24h; }
    public double getPercent_change_7d() { return this.percent_change_7d; }

    // Setters
    public void setId(int newId) { this.id = newId; }

    public void setName(String newName) { this.name = newName; }
    public void setSymbol(String newSymbol) { this.symbol = newSymbol; }

    public void setPrice(double newPrice) { this.price = newPrice; }
    public void setPercent_change_1h(double newPercent_change_1h) { this.percent_change_1h = newPercent_change_1h; }
    public void setPercent_change_24h(double newPercent_change_24h) { this.percent_change_24h = newPercent_change_24h; }
    public void setPercent_change_7d(double newPercent_change_7d) { this.percent_change_7d = newPercent_change_7d; }

    public CryptoModel(JSONObject jsonObject) throws JSONException {

        name = jsonObject.getString(String.valueOf(R.string.name));
        symbol = jsonObject.getString(String.valueOf(symbol));
        price = jsonObject.getDouble(String.valueOf(price));
        percent_change_1h = jsonObject.getDouble(String.valueOf(percent_change_1h));
        percent_change_24h = jsonObject.getDouble(String.valueOf(percent_change_24h));
        percent_change_7d = jsonObject.getDouble(String.valueOf(percent_change_7d));
    }

    public static List<CryptoModel> fromJsonArray(JSONArray cryptoJsonArray) throws URISyntaxException, JSONException {
        List<CryptoModel> crypto = new ArrayList<>();
        for (int i = 0; i < cryptoJsonArray.length(); i++) {
            crypto.add(new CryptoModel(cryptoJsonArray.getJSONObject(i)));
        }
        return crypto;
    }
}
