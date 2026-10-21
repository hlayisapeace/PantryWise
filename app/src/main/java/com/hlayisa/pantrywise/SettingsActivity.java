package com.hlayisa.pantrywise;
import android.os.Bundle;
import android.widget.*;
public class SettingsActivity extends Screen {
    @Override public void onCreate(Bundle s){super.onCreate(s);layout("Settings");Switch expiry=new Switch(this);expiry.setText("Expiry warnings");expiry.setChecked(getSharedPreferences("prefs",0).getBoolean("expiry",true));expiry.setOnCheckedChangeListener((v,enabled)->getSharedPreferences("prefs",0).edit().putBoolean("expiry",enabled).apply());body.addView(expiry);body.addView(text("PantryWise 1.0",16));}
}
