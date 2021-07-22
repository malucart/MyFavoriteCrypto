package com.example.luizacryptotracker;

import android.widget.ImageView;

import java.util.ArrayList;

public class CryptoModel {

    private ArrayList logo;
    private ArrayList name;
    private ArrayList symbol;
    private ArrayList price;
    private ArrayList oneHour;
    private ArrayList twentyFourHour;
    private ArrayList oneWeek;

    public CryptoModel(ArrayList logo, ArrayList name, ArrayList symbol, ArrayList price, ArrayList oneHour, ArrayList twentyFourHour, ArrayList oneWeek) {
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
    public ArrayList getName() { return name; }
    public ArrayList getSymbol() { return symbol; }
    public ArrayList getPrice() { return price; }
    public ArrayList getOneHour() { return oneHour; }
    public ArrayList getTwentyFourHour() { return twentyFourHour; }
    public ArrayList getOneWeek() { return oneWeek; }

    // Setters
    public void setLogo(ArrayList newLogo) { this.logo = newLogo; }
    public void setName(ArrayList newName) { this.name = newName; }
    public void setSymbol(ArrayList newSymbol) { this.symbol = newSymbol; }
    public void setPrice(ArrayList newPrice) { this.price = newPrice; }
    public void setOneHour(ArrayList newOneHour) { this.price = newOneHour; }
    public void setTwentyFourHour(ArrayList newTwentyFourHour) { this.twentyFourHour = newTwentyFourHour; }
    public void setOneWeek(ArrayList newOneWeek) { this.oneWeek = newOneWeek; }
}