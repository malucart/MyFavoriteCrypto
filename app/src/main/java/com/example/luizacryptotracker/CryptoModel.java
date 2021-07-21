package com.example.luizacryptotracker;

import android.widget.ImageView;

public class CryptoModel {

    private String symbol;
    private String name;
    private double price;
    private double oneHour;
    private double twentyFourHour;
    private double oneWeek;

    public CryptoModel(String name, String symbol, double price, double oneHour, double twentyFourHour, double oneWeek) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
        this.oneHour = oneHour;
        this.twentyFourHour = twentyFourHour;
        this.oneWeek = oneWeek;
    }

    // Getters
    public String getSymbol() { return symbol; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public double getOneHour() { return oneHour; }
    public double getTwentyFourHour() { return twentyFourHour; }
    public double getOneWeek() { return oneWeek; }

    // Setters
    public void setSymbol(String newSymbol) { this.symbol = newSymbol; }
    public void setName(String newName) { this.name = newName; }
    public void setPrice(double newPrice) { this.price = newPrice; }
    public void setOneHour(double newOneHour) { this.price = newOneHour; }
    public void setTwentyFourHour(double newTwentyFourHour) { this.twentyFourHour = newTwentyFourHour; }
    public void setOneWeek(double newOneWeek) { this.oneWeek = newOneWeek; }
}