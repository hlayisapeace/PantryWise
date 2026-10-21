package com.hlayisa.pantrywise;
import android.os.Bundle;
import android.content.Intent;
import android.view.*;
import android.widget.*;
import java.util.*;

public class RecipesActivity extends Screen {
    @Override public void onCreate(Bundle s){super.onCreate(s);layout("Suggested recipes");}
    @Override public void onResume(){super.onResume();body.removeAllViews();List<Meal> matches=new ArrayList<>();List<Stock> pantry=db.stock();for(Meal m:db.meals())if(Matcher.qualifies(m,pantry))matches.add(m);
        body.addView(text(matches.size()+" ready to cook",18));if(matches.isEmpty()){body.addView(text("No recipes match your pantry yet. Add more ingredients.",16));return;}
        ListView list=new ListView(this);list.setAdapter(new BaseAdapter(){public int getCount(){return matches.size();}public Meal getItem(int p){return matches.get(p);}public long getItemId(int p){return getItem(p).id;}
            public View getView(int p,View old,ViewGroup parent){LinearLayout row=new LinearLayout(RecipesActivity.this);row.setOrientation(1);row.setPadding(dp(8),dp(8),dp(8),dp(8));row.addView(text(getItem(p).name,18));row.addView(text("All "+getItem(p).requirements.size()+" ingredients available",14));return row;}});
        body.addView(list,new LinearLayout.LayoutParams(-1,0,1));list.setOnItemClickListener((p,v,pos,id)->startActivity(new Intent(this,DetailActivity.class).putExtra("meal_id",matches.get(pos).id)));
    }
}
