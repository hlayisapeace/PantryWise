package com.hlayisa.pantrywise;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.widget.*;
import java.text.*;
import java.util.*;

public abstract class Screen extends Activity {
    protected LinearLayout body; protected PantryDb db;
    protected void layout(String title) {
        db=new PantryDb(this);LinearLayout root=new LinearLayout(this);root.setOrientation(1);root.setBackgroundColor(Color.WHITE);
        root.setOnApplyWindowInsetsListener((v,i)->{v.setPadding(dp(16),dp(12)+i.getSystemWindowInsetTop(),dp(16),dp(12)+i.getSystemWindowInsetBottom());return i;});
        TextView heading=text(title,24);heading.setTextColor(Color.rgb(0,105,110));root.addView(heading);
        body=new LinearLayout(this);body.setOrientation(1);root.addView(body,new LinearLayout.LayoutParams(-1,0,1));
        LinearLayout nav=new LinearLayout(this);String[] labels={"Pantry","Recipes","Settings"};Class<?>[] targets={HomeActivity.class,RecipesActivity.class,SettingsActivity.class};
        for(int j=0;j<3;j++){final Class<?> target=targets[j];Button b=button(labels[j],()->{if(getClass()!=target)startActivity(new Intent(this,target).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));});b.setTextSize(12);nav.addView(b,new LinearLayout.LayoutParams(0,dp(52),1));}
        root.addView(nav);setContentView(root);
    }
    protected TextView text(String s,int size){TextView t=new TextView(this);t.setText(s);t.setTextSize(size);t.setTextColor(Color.rgb(29,47,49));t.setPadding(0,dp(8),0,dp(8));return t;}
    protected Button button(String title,Runnable action){Button b=new Button(this);b.setText(title);b.setAllCaps(false);b.setOnClickListener(v->action.run());return b;}
    protected int dp(int n){return Math.round(n*getResources().getDisplayMetrics().density);}
    protected String qty(double n){return n==Math.rint(n)?Long.toString((long)n):Double.toString(n);}
    protected Date date(String s){if(!s.matches("\\d{4}-\\d{2}-\\d{2}"))return null;SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd",Locale.ROOT);f.setLenient(false);try{return f.parse(s);}catch(ParseException ex){return null;}}
    protected String warning(Stock s){
        if(!getSharedPreferences("prefs",0).getBoolean("expiry",true)||s.expiry.isEmpty())return "";
        Date d=date(s.expiry);if(d==null)return "";Calendar today=Calendar.getInstance();today.set(Calendar.HOUR_OF_DAY,0);today.set(Calendar.MINUTE,0);today.set(Calendar.SECOND,0);today.set(Calendar.MILLISECOND,0);
        Calendar soon=(Calendar)today.clone();soon.add(Calendar.DAY_OF_YEAR,3);
        return d.before(today.getTime())?"Expired - check before use":!d.after(soon.getTime())?"Expires within 3 days":"";
    }
    @Override protected void onDestroy(){if(db!=null)db.close();super.onDestroy();}
}
