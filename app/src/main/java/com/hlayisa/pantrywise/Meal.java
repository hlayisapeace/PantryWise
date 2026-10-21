package com.hlayisa.pantrywise;

import java.util.ArrayList;
import java.util.List;

public final class Meal {
    public long id;
    public final String name, method;
    public final List<Stock> requirements = new ArrayList<>();
    public Meal(long id, String name, String method) {
        this.id = id; this.name = name; this.method = method;
    }
}
