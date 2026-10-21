package com.hlayisa.pantrywise;
import android.os.Bundle;
import android.app.AlertDialog;
import android.text.InputType;
import android.widget.*;

public class IngredientActivity extends Screen {
    private EditText name,quantity,expiry;private Spinner unit;private long id;
    private final String[] units={"item","g","kg","ml","l","tsp","tbsp","slice"};
    @Override public void onCreate(Bundle state){super.onCreate(state);id=getIntent().getLongExtra("stock_id",-1);layout(id==-1?"Add ingredient":"Edit ingredient");
        ScrollView scroll=new ScrollView(this);LinearLayout form=new LinearLayout(this);form.setOrientation(1);scroll.addView(form);body.addView(scroll);
        name=input(form,"Ingredient name",InputType.TYPE_CLASS_TEXT);quantity=input(form,"Quantity",InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
        form.addView(text("Unit",14));unit=new Spinner(this);unit.setId(10001);unit.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,units));form.addView(unit);
        expiry=input(form,"Expiry (optional, YYYY-MM-DD)",InputType.TYPE_CLASS_DATETIME);
        if(id!=-1){Stock s=db.stock(id);if(s==null){finish();return;}name.setText(s.name);quantity.setText(qty(s.quantity));expiry.setText(s.expiry);for(int i=0;i<units.length;i++)if(units[i].equals(s.unit))unit.setSelection(i);}
        form.addView(button("Save ingredient",this::save));
        if(id!=-1)form.addView(button("Delete ingredient",()->new AlertDialog.Builder(this).setTitle("Delete ingredient?").setMessage("Remove this pantry item?").setNegativeButton("Cancel",null).setPositiveButton("Delete",(d,w)->{if(db.delete(id)){Toast.makeText(this,"Ingredient deleted",0).show();finish();}else Toast.makeText(this,"Delete failed",0).show();}).show()));
    }
    private EditText input(LinearLayout form,String label,int type){form.addView(text(label,14));EditText e=new EditText(this);e.setId(label.hashCode() & 0x00ffffff);e.setInputType(type);e.setSingleLine(true);e.setContentDescription(label);form.addView(e);return e;}
    private void save(){String n=name.getText().toString().trim(),q=quantity.getText().toString().trim(),e=expiry.getText().toString().trim();
        if(n.isEmpty()){name.setError("Enter an ingredient name");return;}double value;try{value=Double.parseDouble(q);}catch(NumberFormatException ex){quantity.setError("Enter a positive quantity");return;}
        if(Double.isNaN(value)||Double.isInfinite(value)||value<=0){quantity.setError("Quantity must be finite and above zero");return;}
        if(!e.isEmpty()&&date(e)==null){expiry.setError("Enter a real date in YYYY-MM-DD format");return;}
        try{if(db.save(id,n,value,unit.getSelectedItem().toString(),e)==-1){Toast.makeText(this,"Save failed",0).show();return;}Toast.makeText(this,id==-1?"Ingredient added":"Ingredient updated",0).show();finish();}catch(android.database.SQLException ex){Toast.makeText(this,"Unable to save. Please retry.",0).show();}
    }
}
