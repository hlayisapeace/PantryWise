package com.hlayisa.pantrywise;

public final class Stock {
    public long id;
    public final String name, unit, expiry;
    public final double quantity;
    public Stock(long id, String name, double quantity, String unit, String expiry) {
        this.id = id; this.name = name; this.quantity = quantity; this.unit = unit; this.expiry = expiry;
    }
}
