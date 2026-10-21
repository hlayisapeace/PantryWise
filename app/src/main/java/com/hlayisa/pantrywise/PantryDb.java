package com.hlayisa.pantrywise;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.*;
import java.util.*;

public final class PantryDb extends SQLiteOpenHelper {
    public PantryDb(Context context) { super(context, "pantrywise.db", null, 1); }
    @Override public void onConfigure(SQLiteDatabase db) { db.setForeignKeyConstraintsEnabled(true); }
    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE stock(id INTEGER PRIMARY KEY, name TEXT NOT NULL, quantity REAL NOT NULL CHECK(quantity > 0), unit TEXT NOT NULL, expiry TEXT NOT NULL DEFAULT '')");
        db.execSQL("CREATE TABLE meal(id INTEGER PRIMARY KEY, name TEXT NOT NULL UNIQUE, method TEXT NOT NULL)");
        db.execSQL("CREATE TABLE requirement(id INTEGER PRIMARY KEY, meal_id INTEGER NOT NULL REFERENCES meal(id) ON DELETE CASCADE, name TEXT NOT NULL, quantity REAL NOT NULL CHECK(quantity > 0), unit TEXT NOT NULL)");
        db.execSQL("CREATE INDEX requirement_meal ON requirement(meal_id)");
        seed(db);
    }
    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new IllegalStateException("A data-preserving migration is required for this schema change");
    }
    public long save(long id, String name, double quantity, String unit, String expiry) {
        ContentValues v = new ContentValues(); v.put("name", name.trim()); v.put("quantity", quantity);
        v.put("unit", Matcher.unit(unit)); v.put("expiry", expiry);
        if(id == -1) return getWritableDatabase().insertOrThrow("stock", null, v);
        return getWritableDatabase().update("stock", v, "id=?", new String[]{""+id}) == 1 ? id : -1;
    }
    public boolean delete(long id) { return getWritableDatabase().delete("stock", "id=?", new String[]{""+id}) == 1; }
    public List<Stock> stock() {
        List<Stock> result = new ArrayList<>();
        try(Cursor c = getReadableDatabase().rawQuery("SELECT id,name,quantity,unit,expiry FROM stock ORDER BY name COLLATE NOCASE,id", null)) {
            while(c.moveToNext()) result.add(new Stock(c.getLong(0),c.getString(1),c.getDouble(2),c.getString(3),c.getString(4)));
        }
        return result;
    }
    public Stock stock(long id) { for(Stock s : stock()) if(s.id == id) return s; return null; }
    public List<Meal> meals() {
        List<Meal> result = new ArrayList<>();
        try(Cursor c = getReadableDatabase().rawQuery("SELECT id,name,method FROM meal ORDER BY name", null)) {
            while(c.moveToNext()) {
                Meal meal = new Meal(c.getLong(0), c.getString(1), c.getString(2));
                try(Cursor r = getReadableDatabase().rawQuery("SELECT name,quantity,unit FROM requirement WHERE meal_id=? ORDER BY id", new String[]{""+meal.id})) {
                    while(r.moveToNext()) meal.requirements.add(new Stock(-1,r.getString(0),r.getDouble(1),r.getString(2),""));
                }
                result.add(meal);
            }
        }
        return result;
    }
    public Meal meal(long id) { for(Meal m : meals()) if(m.id == id) return m; return null; }
    private void recipe(SQLiteDatabase db, String name, String method, String... requirements) {
        ContentValues v = new ContentValues(); v.put("name",name); v.put("method", method);
        long id = db.insertOrThrow("meal",null,v);
        for(String line : requirements) {
            String[] p = line.split("\\|"); ContentValues r = new ContentValues();
            r.put("meal_id",id); r.put("name",p[0]); r.put("quantity",Double.parseDouble(p[1])); r.put("unit",p[2]);
            db.insertOrThrow("requirement",null,r);
        }
    }
    private void seed(SQLiteDatabase db) {
        recipe(db,"Tomato skillet eggs","1. Chop the tomato into a non-stick pan.\n2. Heat gently until softened, stirring frequently.\n3. Beat in the eggs and cook until completely set.","tomato|1|item","egg|2|item");
        recipe(db,"Banana breakfast bowl","1. Spoon yogurt into a bowl.\n2. Slice the banana.\n3. Scatter oats on top and serve.","yogurt|150|g","banana|1|item","oats|30|g");
        recipe(db,"Cheddar toast","1. Place cheese on bread.\n2. Grill until cheese melts and bread is toasted.","bread|2|slice","cheese|40|g");
        recipe(db,"Bean rice bowl","1. Combine cooked rice and cooked beans.\n2. Heat until steaming hot throughout and serve immediately.","cooked rice|150|g","cooked beans|120|g");
        recipe(db,"Tomato pasta bowl","1. Warm cooked pasta with tomato sauce.\n2. Heat until steaming hot throughout.","cooked pasta|150|g","tomato sauce|100|ml");
        recipe(db,"Chicken lettuce bowl","1. Wash and dry lettuce.\n2. Slice cooked chicken and cucumber.\n3. Combine and serve chilled.","cooked chicken|120|g","lettuce|50|g","cucumber|1|item");
        recipe(db,"Peanut toast","1. Toast bread.\n2. Spread peanut butter on top.","bread|2|slice","peanut butter|30|g");
        recipe(db,"Root vegetable soup","1. Chop potato, carrot and onion.\n2. Simmer in stock until vegetables are tender.\n3. Mash lightly and serve.","potato|2|item","carrot|2|item","onion|1|item","stock|500|ml");
        recipe(db,"Apple yogurt","1. Wash and chop the apple.\n2. Stir into yogurt and serve.","apple|1|item","yogurt|150|g");
        recipe(db,"Garlic butter pasta","1. Melt butter and cook minced garlic gently.\n2. Toss with cooked pasta until steaming hot.","cooked pasta|150|g","butter|20|g","garlic|2|item");
        recipe(db,"Potato egg pan","1. Slice cooked potato into a non-stick pan.\n2. Warm gently.\n3. Add beaten eggs and cook until set.","cooked potato|100|g","egg|2|item");
        recipe(db,"Apple cinnamon oats","1. Heat oats and milk with chopped apple.\n2. Simmer while stirring until oats are soft.\n3. Stir in cinnamon.","oats|50|g","milk|200|ml","apple|1|item","cinnamon|1|tsp");
        recipe(db,"Tomato rice","1. Combine cooked rice and tomato sauce.\n2. Heat until steaming hot throughout.","cooked rice|150|g","tomato sauce|100|ml");
        recipe(db,"Pea egg rice","1. Heat oil in a pan and scramble the egg until set.\n2. Add cooked peas and cooked rice.\n3. Stir until steaming hot throughout.","cooked rice|150|g","egg|1|item","cooked peas|50|g","oil|1|tbsp");
        recipe(db,"Cucumber tomato salad","1. Wash and chop cucumber and tomato.\n2. Toss with oil and serve.","cucumber|1|item","tomato|1|item","oil|1|tbsp");
        recipe(db,"Three ingredient pancakes","1. Whisk flour, milk and egg until smooth.\n2. Spoon onto a non-stick pan.\n3. Cook both sides until golden and cooked through.","flour|120|g","milk|200|ml","egg|1|item");
        recipe(db,"Creamy potato mash","1. Warm cooked potatoes until steaming hot.\n2. Mash with milk and butter.","cooked potato|300|g","milk|50|ml","butter|20|g");
        recipe(db,"Beans on toast","1. Toast bread.\n2. Heat cooked beans until steaming hot.\n3. Spoon beans over toast.","bread|2|slice","cooked beans|120|g");
        recipe(db,"Chicken pea rice","1. Combine cooked chicken, cooked rice and cooked peas.\n2. Heat until steaming hot throughout and serve immediately.","cooked chicken|120|g","cooked rice|150|g","cooked peas|50|g");
        recipe(db,"Banana milk blend","1. Peel banana.\n2. Blend with milk and yogurt until smooth.","banana|1|item","milk|100|ml","yogurt|120|g");
    }
}
