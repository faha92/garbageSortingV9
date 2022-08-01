package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class ItemBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    public static final String DATABASE_NAME = "garbage.db";

    public ItemBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ItemsDbSchema.ItemTable.NAME + "(" +
                ItemsDbSchema.ItemTable.Cols.WHAT + ", " + ItemsDbSchema.ItemTable.Cols.WHERE + ", " + ItemsDbSchema.ItemTable.Cols.PICT + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}