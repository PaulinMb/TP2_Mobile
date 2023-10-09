package com.example.mainactivity_mapsactivity.modele;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {
    public static final String TABLE_DOMINOS_PIZZA = "dominos_pizza";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ADRESSE = "adresse";
    public static final String COLUMN_NUMERO_TELEPHONE = "numero_telephone";

    private static final String DATABASE_NAME = "domino's_pizza.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table " + TABLE_DOMINOS_PIZZA + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_ADRESSE + " text not null, "
            + COLUMN_NUMERO_TELEPHONE + " text not null);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Mise à niveau de la base de données à partir de la version  " + oldVersion + " à "
                        + newVersion + ", qui détruira toutes les anciennes données");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOMINOS_PIZZA);
        onCreate(db);
    }
}
