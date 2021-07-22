package com.example.luizacryptotracker;

import android.widget.ImageView;

import java.util.ArrayList;

public class CryptoModel {

    private ArrayList logo;
    private String name;
    private String symbol;
    private double price;
    private double oneHour;
    private double twentyFourHour;
    private double oneWeek;

    public CryptoModel(ArrayList logo, String name, String symbol, double price, double oneHour, double twentyFourHour, double oneWeek) {
        this.logo = logo;
        this.name = name;
        this.symbol = symbol;
        this.price = price;
        this.oneHour = oneHour;
        this.twentyFourHour = twentyFourHour;
        this.oneWeek = oneWeek;
    }

    // Getters
    public ArrayList getLogo() { return logo; }
    public String getName() { return name; }
    public String getSymbol() { return symbol; }
    public double getPrice() { return price; }
    public double getOneHour() { return oneHour; }
    public double getTwentyFourHour() { return twentyFourHour; }
    public double getOneWeek() { return oneWeek; }

    // Setters
    public void setLogo(ArrayList newLogo) { this.logo = newLogo; }
    public void setName(String newName) { this.name = newName; }
    public void setSymbol(String newSymbol) { this.symbol = newSymbol; }
    public void setPrice(double newPrice) { this.price = newPrice; }
    public void setOneHour(double newOneHour) { this.price = newOneHour; }
    public void setTwentyFourHour(double newTwentyFourHour) { this.twentyFourHour = newTwentyFourHour; }
    public void setOneWeek(double newOneWeek) { this.oneWeek = newOneWeek; }
}