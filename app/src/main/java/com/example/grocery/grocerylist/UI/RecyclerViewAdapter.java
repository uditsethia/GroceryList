package com.example.grocery.grocerylist.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.example.grocery.grocerylist.Activity.DetailsActivity;
//import com.example.grocery.grocerylist.Activity.MainActivity;
import com.example.grocery.grocerylist.Data.DatabaseHandler;
import com.example.grocery.grocerylist.Model.Grocery;
import com.example.grocery.grocerylist.R;

import java.text.DateFormat;
import java.util.List;
import java.util.zip.Inflater;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Grocery> groceries;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private LayoutInflater layoutInflater;

    public RecyclerViewAdapter(Context context, List<Grocery> groceries) {
        this.context = context;
        this.groceries = groceries;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row,viewGroup,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder viewHolder, int i) {
        //Data Binding
        Grocery grocery = groceries.get(i);
        viewHolder.Itm.setText(grocery.getGroceryName());
        viewHolder.Qty.setText(grocery.getGroceryQty());
        viewHolder.Date.setText(grocery.getDateOfAdd());
    }

    @Override
    public int getItemCount() {
        return groceries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView Qty;
        public TextView Itm;
        public TextView Date;
        public int id;
        public Button Delete;
        public Button Edit;

        public ViewHolder(@NonNull View itemView,Context ctx) {
            super(itemView);
            context = ctx;

            Qty = (TextView)itemView.findViewById(R.id.listTextQty);
            Date = (TextView)itemView.findViewById(R.id.Date);
            Itm = (TextView)itemView.findViewById(R.id.listTextName);
            Delete = (Button) itemView.findViewById(R.id.deleteButton);
            Edit = (Button)itemView.findViewById(R.id.editButton);

            Edit.setOnClickListener(this);
            Delete.setOnClickListener(this);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Grocery grocery = groceries.get(position);
                    Intent intent = new Intent(context,DetailsActivity.class);
                    intent.putExtra("name",grocery.getGroceryName());
                    intent.putExtra("quantity",grocery.getGroceryQty());
                    intent.putExtra("id",grocery.getId());
                    intent.putExtra("date",grocery.getDateOfAdd());
                    context.startActivity(intent);
                }
            });
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.editButton:{
                    int num = getAdapterPosition();
                    Grocery grocery = groceries.get(num);
                    updateItem(grocery);
                }
                break;
                case R.id.deleteButton:{
                    int pos=getAdapterPosition();
                    Grocery grocery = groceries.get(pos);
                    deleteItem(grocery.getId());
                }
                break;
            }
        }
        public void deleteItem(final int id){
            alertDialogBuilder = new AlertDialog.Builder(context);
            layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.confirmation_dialog,null);
            Button no = (Button)view.findViewById(R.id.no);
            Button yes = (Button)view.findViewById(R.id.yes);
            alertDialogBuilder.setView(view);
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
//            this Yes creation and no creation acts as inner class and therfore we need to make our id final in
//            order to use it.
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHandler db= new DatabaseHandler(context);
                    db.deleteGrocery(id);
                    groceries.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    alertDialog.dismiss();
                }
            });
        }
        public void updateItem(final Grocery grocery){
            alertDialogBuilder = new AlertDialog.Builder(context);
            layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.popup,null);
            final EditText groceryName = (EditText)view.findViewById(R.id.groceryItem);
            final EditText groceryQty = (EditText)view.findViewById(R.id.groceryQty);
            final TextView head = (TextView)view.findViewById(R.id.tile);
            Button save = (Button)view.findViewById(R.id.save);
            head.setText("Edit Grocery");
            alertDialogBuilder.setView(view);
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHandler db = new DatabaseHandler(context);
                    grocery.setGroceryName(groceryName.getText().toString());
                    grocery.setGroceryQty(groceryQty.getText().toString());

                    if(!groceryName.toString().equals("") && !groceryQty.toString().equals("")){
                        db.update(grocery);
                        notifyItemChanged(getAdapterPosition(),grocery);
                    } else {
                        Snackbar.make(v,"Add the details",Snackbar.LENGTH_SHORT).show();
                    }
                    alertDialog.dismiss();
                }

            });
        }
    }
}
