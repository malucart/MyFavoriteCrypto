package com.luiza.luizacryptotracker;

public class CryptoModel {

    private String name;
    private String symbol;
    private String logoURL;
    private Double price;
    private Double oneHour;
    private Double twentyFourHour;
    private Double oneWeek;

    public CryptoModel(String name, String symbol, Double price, Double oneHour, Double twentyFourHour, Double oneWeek) {
        this.name = name;
        this.symbol = symbol;
        this.logoURL = "";
        this.price = price;
        this.oneHour = oneHour;
        this.twentyFourHour = twentyFourHour;
        this.oneWeek = oneWeek;
    }

    // Getters
    public String getName() { return name; }
    public String getSymbol() { return symbol; }
    public String getLogoURL() { return logoURL; }
    public Double getPrice() { return price; }
    public Double getOneHour() { return oneHour; }
    public Double getTwentyFourHour() { return twentyFourHour; }
    public Double getOneWeek() { return oneWeek; }

    // Setters
    public void setName(String newName) { this.name = newName; }
    public void setSymbol(String newSymbol) { this.symbol = newSymbol; }
    public void setLogoURL(String newURL) {this.logoURL = newURL; }
    public void setPrice(Double newPrice) { this.price = newPrice; }
    public void setOneHour(Double newOneHour) { this.price = newOneHour; }
    public void setTwentyFourHour(Double newTwentyFourHour) { this.twentyFourHour = newTwentyFourHour; }
    public void setOneWeek(Double newOneWeek) { this.oneWeek = newOneWeek; }
}