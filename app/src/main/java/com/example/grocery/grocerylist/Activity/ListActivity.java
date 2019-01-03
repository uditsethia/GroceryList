package com.example.grocery.grocerylist.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.grocery.grocerylist.Data.DatabaseHandler;
import com.example.grocery.grocerylist.Model.Grocery;
import com.example.grocery.grocerylist.R;
import com.example.grocery.grocerylist.UI.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog alertDialog;

    private EditText groceryQty;
    private EditText getGroceryItem;
    private Button saveButton;

    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private List<Grocery> groceryList;
    private List<Grocery> listItem;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new DatabaseHandler(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup();
            }
        });


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        groceryList = new ArrayList<>();
        listItem = new ArrayList<>();

        //get all db values
        groceryList=db.getAllGrocery();
//        int x= db.getCount();
        for(Grocery g : groceryList){
            Grocery grocery = new Grocery();
            grocery.setGroceryName("Name: "+g.getGroceryName());
            Log.d("List",g.getGroceryName());
            grocery.setGroceryQty("Qty: "+g.getGroceryQty());
            grocery.setId(g.getId());
            grocery.setDateOfAdd("Added on: "+g.getDateOfAdd());

            listItem.add(grocery);
        }

        recyclerViewAdapter = new RecyclerViewAdapter(this,listItem);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
//        db.close();
    }
    public void popup(){
        dialogBuilder = new AlertDialog.Builder(this);
        // inflating the popupXML. This is an idea concept.

        View view = getLayoutInflater().inflate(R.layout.popup,null);
        groceryQty= view.findViewById(R.id.groceryQty);
        getGroceryItem = view.findViewById(R.id.groceryItem);
        saveButton = view.findViewById(R.id.save);

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
    public void saveGroceryToDB(View v){
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
                Intent intent = getIntent();
                finish();
                startActivity(intent);
//                startActivity(new Intent(MainActivity.this,ListActivity.class));
            }
        },1000);
    }

}
