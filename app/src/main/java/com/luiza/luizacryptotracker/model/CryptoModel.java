package com.luiza.luizacryptotracker.model;

public class CryptoModel {

    public static final String TAG = "CryptoModel";

    private Boolean favStatus;
    private String name;
    private String symbol;
    private String logoURL;
    private Double price;
    private Double oneHour;
    private Double twentyFourHour;
    private Double oneWeek;
    private String objectId;

    public CryptoModel(String name, String symbol, String logoURL, Double price, Double oneHour, Double twentyFourHour, Double oneWeek) {
        this.favStatus = false;
        this.name = name;
        this.symbol = symbol;
        this.logoURL = logoURL;
        this.price = price;
        this.oneHour = oneHour;
        this.twentyFourHour = twentyFourHour;
        this.oneWeek = oneWeek;
        this.objectId = "";
    }

    public CryptoModel() {

    }


    // Getters
    public Boolean getFavStatus() { return favStatus; }
    public String getName() { return name; }
    public String getSymbol() { return symbol; }
    public String getLogoURL() { return logoURL; }
    public String getObjectId() { return objectId; }
    public Double getPrice() { return price; }
    public Double getOneHour() { return oneHour; }
    public Double getTwentyFourHour() { return twentyFourHour; }
    public Double getOneWeek() { return oneWeek; }

    // Setters
    public void setFavStatus(Boolean favStatus) { this.favStatus = favStatus; }
    public void setName(String newName) { this.name = newName; }
    public void setSymbol(String newSymbol) { this.symbol = newSymbol; }
    public void setLogoURL(String newURL) { this.logoURL = newURL; }
    public void setObjectId(String objectId) { this.objectId = objectId; }
    public void setPrice(Double newPrice) { this.price = newPrice; }
    public void setOneHour(Double newOneHour) { this.oneHour = newOneHour; }
    public void setTwentyFourHour(Double newTwentyFourHour) { this.twentyFourHour = newTwentyFourHour; }
    public void setOneWeek(Double newOneWeek) { this.oneWeek = newOneWeek; }
}