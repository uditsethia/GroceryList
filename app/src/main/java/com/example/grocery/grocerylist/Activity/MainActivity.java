package com.example.grocery.grocerylist.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.grocery.grocerylist.Data.DatabaseHandler;
import com.example.grocery.grocerylist.Model.Grocery;
import com.example.grocery.grocerylist.R;

public class MainActivity extends AppCompatActivity {
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog alertDialog;
    private Button saveButton;
    private EditText groceryQty;
    private EditText getGroceryItem;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db =new DatabaseHandler(this);
        byPassAct();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup();
            }
        });
    }

    private void popup() {
        dialogBuilder = new AlertDialog.Builder(this);
        // inflating the popupXML. This is an idea concept.

        View view = getLayoutInflater().inflate(R.layout.popup,null);
        groceryQty= (EditText) view.findViewById(R.id.groceryQty);
        getGroceryItem = (EditText)view.findViewById(R.id.groceryItem);
        saveButton = (Button)view.findViewById(R.id.save);

        dialogBuilder.setView(view);
        alertDialog = dialogBuilder.create();
        alertDialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Snackbar.make(v,"hihh",Snackbar.LENGTH_SHORT).show();
                if(!getGroceryItem.getText().toString().isEmpty() && !groceryQty.getText().toString().isEmpty())
                    saveGroceryToDB(v);
            }
        });
    }

    private void saveGroceryToDB(View v) {
        Grocery grocery =  new Grocery();

        String item = getGroceryItem.getText().toString();
        String qty = groceryQty.getText().toString();

        grocery.setGroceryQty(qty);
        grocery.setGroceryName(item);

        db.addGrocery(grocery);
        Snackbar.make(v,"Saved Succesful",Snackbar.LENGTH_LONG).show();
//        Log.d("Item in Db",String.valueOf(db.getCount()));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
                startActivity(new Intent(MainActivity.this,ListActivity.class));
            }
        },1000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void byPassAct(){
        if(db.getCount()>0){
            startActivity(new Intent(MainActivity.this, ListActivity.class));
            finish();
        }
    }
}
