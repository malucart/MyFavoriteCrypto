package com.example.luizacryptotracker;

public class CryptoModel {

    private String symbol;
    private String name;
    private double price;

    public CryptoModel(String name, String symbol, double price) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
    }

    // Getters
    public String getSymbol() { return symbol; }
    public String getName() { return name; }
    public double getPrice() { return price; }

    // Setters
    public void setSymbol(String newSymbol) { this.symbol = newSymbol; }
    public void setName(String newName) { this.name = newName; }
    public void setPrice(double newPrice) { this.price = newPrice; }
}