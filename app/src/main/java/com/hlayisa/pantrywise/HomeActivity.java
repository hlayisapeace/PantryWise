package com.hlayisa.pantrywise;
import android.os.Bundle;
import android.content.Intent;
import android.view.*;
import android.widget.*;
import java.util.*;

public class HomeActivity extends Screen {
    @Override public void onCreate(Bundle s){super.onCreate(s);layout("PantryWise");}
    @Override public void onResume(){super.onResume();body.removeAllViews();List<Stock> rows=db.stock();
        body.addView(text("Your pantry / "+rows.size()+" items",18));body.addView(button("Add ingredient",()->startActivity(new Intent(this,IngredientActivity.class))));
        if(rows.isEmpty()){body.addView(text("Your pantry is empty.",16));return;}
        ListView list=new ListView(this);list.setAdapter(new BaseAdapter(){
            public int getCount(){return rows.size();}public Stock getItem(int p){return rows.get(p);}public long getItemId(int p){return getItem(p).id;}
            public View getView(int p,View old,ViewGroup parent){Stock s=getItem(p);LinearLayout row=new LinearLayout(HomeActivity.this);row.setOrientation(1);row.setPadding(dp(8),dp(8),dp(8),dp(8));row.addView(text(s.name,18));row.addView(text(qty(s.quantity)+" "+s.unit+(s.expiry.isEmpty()?"":" | "+s.expiry),14));String w=warning(s);if(!w.isEmpty()){TextView alert=text(w,14);alert.setTextColor(0xffa43631);row.addView(alert);}return row;}
        });body.addView(list,new LinearLayout.LayoutParams(-1,0,1));list.setOnItemClickListener((p,v,pos,id)->startActivity(new Intent(this,IngredientActivity.class).putExtra("stock_id",rows.get(pos).id)));
    }
}
