package com.example.grocery.grocerylist.Model;

public class Grocery {
    private String GroceryName;
    private String GroceryQty;
    private String DateOfAdd;
    int id;

    public Grocery() {
    }

    public Grocery(String groceryName, String groceryQty, String dateOfAdd, int id) {
        GroceryName = groceryName;
        GroceryQty = groceryQty;
        DateOfAdd = dateOfAdd;
        this.id = id;
    }

    public String getGroceryName() {
        return GroceryName;
    }

    public void setGroceryName(String groceryName) {
        GroceryName = groceryName;
    }

    public String getGroceryQty() {
        return GroceryQty;
    }

    public void setGroceryQty(String groceryQty) {
        GroceryQty = groceryQty;
    }

    public String getDateOfAdd() {
        return DateOfAdd;
    }

    public void setDateOfAdd(String dateOfAdd) {
        DateOfAdd = dateOfAdd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
