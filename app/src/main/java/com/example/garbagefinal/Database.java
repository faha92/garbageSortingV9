package com.example.garbagefinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.lifecycle.ViewModel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import database.ItemBaseHelper;
import database.ItemCursorWrapper;
import database.ItemsDbSchema;

/* A list of items.
 *
 * The Database is a ViewModel and has a single field which is an SQLiteDatabase.
 */

public class Database extends ViewModel {
    private static SQLiteDatabase DB;

    public void initialize(Context context) {
        if (DB == null) {
            DB = new ItemBaseHelper(context.getApplicationContext()).getWritableDatabase();
            if (getValues().size() == 0) fillDatabase(context, "garbageDB.txt");
        }
    }

    //From when using assets. I don't have that now
    public void fillDatabase(Context context, String filename) {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(filename)));
            String line = reader.readLine();
            while (line != null) {
                String[] gItem= line.split(",");
                addItem(gItem[0], gItem[1]);
                line= reader.readLine();
            }
        } catch (IOException e) {  // Error occurred when opening raw file for reading.
        }
    }

    public void addItem(String what, String where){
        removeItem(what);
        Item newItem = new Item(what, where);
        ContentValues values = getContentValues(newItem);
        DB.insert(ItemsDbSchema.ItemTable.NAME, null, values);
    }

    public void addItem(String what, String where, byte[] pict){
        removeItem(what);
        Item newItem = new Item(what, where, pict);
        ContentValues values = getContentValues(newItem);
        DB.insert(ItemsDbSchema.ItemTable.NAME, null, values);
    }

    public void addItem(Item newItem) {
        Log.d("Database", "addItem: starts");
        ContentValues values= getContentValues(newItem);
        DB.insert(ItemsDbSchema.ItemTable.NAME, null, values);
    }


    public void removeItem(String what) {
        String selection= ItemsDbSchema.ItemTable.Cols.WHAT + " = ?";
        int changed= DB.delete(ItemsDbSchema.ItemTable.NAME, selection, new String[]{what});
    }

    public String getWhere(String item) {
        String result = "not found";
        String whatItem = item;

        Cursor cursor = DB.query(ItemsDbSchema.ItemTable.NAME, new String[]{"whereC"},
                ItemsDbSchema.ItemTable.Cols.WHAT + " = ?", new String[]{whatItem},
                null, null, null);

        cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            result = cursor.getString(cursor.getColumnIndex("whereC"));
            // The red line says 'value must be..', but it seems to work anyway. What is wrong?
        }
        return result;
    }

    public ArrayList<Item> getValues() {
        ArrayList<Item> items= new ArrayList<Item>();
        ItemCursorWrapper cursor= queryItems(null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            items.add(cursor.getItem());
            cursor.moveToNext();
        }
        cursor.close();
        return items;
    }


    // Database helper methods to convert between Items and database rows
    private static ContentValues getContentValues(Item item) {
        ContentValues values=  new ContentValues();
        values.put(ItemsDbSchema.ItemTable.Cols.WHAT, item.getWhat());
        values.put(ItemsDbSchema.ItemTable.Cols.WHERE, item.getWhere());
        if (item.getWhere() != null) values.put(ItemsDbSchema.ItemTable.Cols.PICT, item.getPict());
        return values;
    }

    static private ItemCursorWrapper queryItems(String whereClause, String[] whereArgs) {
        Cursor cursor= DB.query(
                ItemsDbSchema.ItemTable.NAME,
                null, // Columns - null selects all columns
                whereClause, whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );
        return new ItemCursorWrapper(cursor);
    }

    public int size() {
        return getValues().size();
    }

    public void close() {   DB.close();   }

}