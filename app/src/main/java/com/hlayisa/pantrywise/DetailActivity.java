package com.hlayisa.pantrywise;
import android.os.Bundle;
import android.widget.*;
public class DetailActivity extends Screen {
    @Override public void onCreate(Bundle s){super.onCreate(s);layout("Recipe");Meal m=db.meal(getIntent().getLongExtra("meal_id",-1));if(m==null){body.addView(text("Recipe not found",18));return;}
        ScrollView scroll=new ScrollView(this);LinearLayout detail=new LinearLayout(this);detail.setOrientation(1);scroll.addView(detail);body.addView(scroll);
        detail.addView(text(m.name,23));detail.addView(text("Ingredients",19));for(Stock ingredient:m.requirements)detail.addView(text(qty(ingredient.quantity)+" "+ingredient.unit+" "+ingredient.name,16));detail.addView(text("Method",19));detail.addView(text(m.method,16));
    }
}
