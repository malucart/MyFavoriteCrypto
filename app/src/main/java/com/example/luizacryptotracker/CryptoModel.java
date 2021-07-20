package com.example.luizacryptotracker;

public class CryptoModel {

    private String symbol;
    private String name;
    private double price;
    private double oneHour;

    public CryptoModel(String name, String symbol, double price, double oneHour) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
        this.oneHour = oneHour;
    }

    // Getters
    public String getSymbol() { return symbol; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public double getOneHour() { return oneHour; }

    // Setters
    public void setSymbol(String newSymbol) { this.symbol = newSymbol; }
    public void setName(String newName) { this.name = newName; }
    public void setPrice(double newPrice) { this.price = newPrice; }
    public void setOneHour(double oneHour) { this.price = oneHour; }
}