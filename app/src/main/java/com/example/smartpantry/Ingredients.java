package com.example.smartpantry;

public class Ingredients {
    private long id;
    private String name;
    private double quantity;
    private String unit;
    private String expiryDate;

    public Ingredients(){}

    public Ingredients(long id, String name, double quantity, String unit, String expiryDate){
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
    }
}
