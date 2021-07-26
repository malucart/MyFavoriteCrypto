package com.luiza.luizacryptotracker;

import android.widget.ImageButton;

public class CryptoModel {

    private String name;
    private String symbol;
    private String logoURL;
    private Double price;
    private Double oneHour;
    private Double twentyFourHour;
    private Double oneWeek;
    private ImageButton ibLike;

    public CryptoModel(String name, String symbol, String logoURL, Double price, Double oneHour, Double twentyFourHour, Double oneWeek) {
        this.name = name;
        this.symbol = symbol;
        this.logoURL = logoURL;
        this.price = price;
        this.oneHour = oneHour;
        this.twentyFourHour = twentyFourHour;
        this.oneWeek = oneWeek;
        this.ibLike = ibLike;
    }

    // Getters
    public String getName() { return name; }
    public String getSymbol() { return symbol; }
    public String getLogoURL() { return logoURL; }
    public Double getPrice() { return price; }
    public Double getOneHour() { return oneHour; }
    public Double getTwentyFourHour() { return twentyFourHour; }
    public Double getOneWeek() { return oneWeek; }
    public ImageButton getIbLike() { return ibLike; }

    // Setters
    public void setName(String newName) { this.name = newName; }
    public void setSymbol(String newSymbol) { this.symbol = newSymbol; }
    public void setLogoURL(String newURL) {this.logoURL = newURL; }
    public void setPrice(Double newPrice) { this.price = newPrice; }
    public void setOneHour(Double newOneHour) { this.price = newOneHour; }
    public void setTwentyFourHour(Double newTwentyFourHour) { this.twentyFourHour = newTwentyFourHour; }
    public void setOneWeek(Double newOneWeek) { this.oneWeek = newOneWeek; }
    public void setIbLike(ImageButton ibLike) { this.ibLike = ibLike; }
}