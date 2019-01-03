package com.example.grocery.grocerylist.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.grocery.grocerylist.R;

public class DetailsActivity extends AppCompatActivity {
    private TextView itemName;
    private TextView quantity;
    private TextView date;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        itemName =(TextView)findViewById(R.id.itemNameDet);
        quantity = (TextView)findViewById(R.id.itemQtyDet);
        date = (TextView)findViewById(R.id.itemDateDet);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            itemName.setText(bundle.getString("name"));
            quantity.setText(bundle.getString("quantity"));
            date.setText(bundle.getString("date"));
            id=bundle.getInt("id");
        }
    }

}
