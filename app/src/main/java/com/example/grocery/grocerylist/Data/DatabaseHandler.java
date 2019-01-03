package com.example.grocery.grocerylist.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.example.grocery.grocerylist.Model.Grocery;
import com.example.grocery.grocerylist.Util.Constants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    Context ctx;
    public DatabaseHandler( Context context) {
        super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
        this.ctx= context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create= "CREATE TABLE "+ Constants.Table_Name +"(" +
                Constants.KEY_ID+" INTEGER PRIMARY KEY,"+Constants.KEY_GROCERY_ITEM+" TEXT,"+
                Constants.KEY_QUANTITY+" TEXT,"+Constants.KEY_DATE_ADDED+" LONG);";
        db.execSQL(create);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ Constants.Table_Name);
        onCreate(db);
    }
    // addGrocery
    public void addGrocery(Grocery grocery){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.KEY_GROCERY_ITEM,grocery.getGroceryName());
        contentValues.put(Constants.KEY_QUANTITY,grocery.getGroceryQty());
        contentValues.put(Constants.KEY_DATE_ADDED,grocery.getDateOfAdd());

        db.insert(Constants.Table_Name,null,contentValues);
        db.close();
    }
    //get a grocery
    public Grocery getGrocery(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Constants.Table_Name,new String[]{Constants.KEY_ID,Constants.KEY_QUANTITY,Constants.KEY_DATE_ADDED,Constants.KEY_GROCERY_ITEM},Constants.KEY_ID +"=?",new String[]{String.valueOf(id)},null,null,null,null);

        Grocery grocery = new Grocery();
        grocery.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
        grocery.setGroceryName(cursor.getString(cursor.getColumnIndex(Constants.KEY_GROCERY_ITEM)));
        grocery.setGroceryQty(cursor.getString(cursor.getColumnIndex(Constants.KEY_QUANTITY)));

        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
        String formatDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_ADDED))).getTime());
        grocery.setDateOfAdd(formatDate);

        return grocery;
    }
    // get a list of grocery
    public List<Grocery> getAllGrocery(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Grocery> groceries= new ArrayList<>();

        try{
            Cursor cursor = db.query(Constants.Table_Name, new String[]{Constants.KEY_ID,Constants.KEY_GROCERY_ITEM,
                    Constants.KEY_QUANTITY,Constants.KEY_DATE_ADDED},null,null,null,null,Constants.KEY_DATE_ADDED + " DESC");
            if(cursor.moveToFirst()){
                do{
                    Grocery grocery = new Grocery();
                    grocery.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))));
                    grocery.setGroceryName(cursor.getString(cursor.getColumnIndex(Constants.KEY_GROCERY_ITEM)));
                    grocery.setGroceryQty(cursor.getString(cursor.getColumnIndex(Constants.KEY_QUANTITY)));

                    java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                    String formatDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_ADDED))).getTime());
                    grocery.setDateOfAdd(formatDate);

                    groceries.add(grocery);

                }while(cursor.moveToNext());
            }

        }catch(Exception e){
            e.printStackTrace();
        }



        return groceries;
    }
    public int update(Grocery grocery){
        SQLiteDatabase db =this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.KEY_GROCERY_ITEM,grocery.getGroceryName());
        contentValues.put(Constants.KEY_QUANTITY,grocery.getGroceryQty());
        contentValues.put(Constants.KEY_DATE_ADDED,grocery.getDateOfAdd());

        return db.update(Constants.Table_Name,contentValues,Constants.KEY_ID + "=?",new String[]{String.valueOf(grocery.getId())});
    }

    //delete an item
    public void deleteGrocery(int id){
        SQLiteDatabase db =this.getWritableDatabase();
        db.delete(Constants.Table_Name,Constants.KEY_ID+ "=?",new String[]{String.valueOf(id)});
        db.close();
    }
    //get count of an item
    public int getCount(){
        String count = "SELECT * FROM "+Constants.Table_Name;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(count,null);

        return cursor.getCount();
    }
}
