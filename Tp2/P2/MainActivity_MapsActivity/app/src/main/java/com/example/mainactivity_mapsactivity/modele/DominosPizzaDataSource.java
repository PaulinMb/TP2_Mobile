package com.example.mainactivity_mapsactivity.modele;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DominosPizzaDataSource {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_ADRESSE, MySQLiteHelper.COLUMN_NUMERO_TELEPHONE };

    public DominosPizzaDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public DominosPizza createDominosPizza(String adresse, String numeroTelephone){
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_ADRESSE, adresse);
        values.put(MySQLiteHelper.COLUMN_NUMERO_TELEPHONE, numeroTelephone);
        long insertId = database.insert(MySQLiteHelper.TABLE_DOMINOS_PIZZA, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_DOMINOS_PIZZA,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        DominosPizza newDominosPizza = cursorToDominosPizza(cursor);
        cursor.close();
        return newDominosPizza;

    }

    public void deleteDominosPizza(DominosPizza dominosPizza) {
        long id = dominosPizza.getId();
        System.out.println("DominosPizza supprimé avec identifiant: " + id);
        database.delete(MySQLiteHelper.TABLE_DOMINOS_PIZZA, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public void deleteAllDominosPiza() {
        System.out.println("Table DominosPizza vidée!");
        database.delete(MySQLiteHelper.TABLE_DOMINOS_PIZZA, null, null);
    }

    public List<DominosPizza> getAllDomniosPizza() {
        List<DominosPizza> dominosPizzas = new ArrayList<DominosPizza>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_DOMINOS_PIZZA,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            DominosPizza dominosPizza = cursorToDominosPizza(cursor);
            dominosPizzas.add(dominosPizza);
            cursor.moveToNext();
        }
        // assurez-vous de la fermeture du curseur
        cursor.close();
        return dominosPizzas;
    }

    private DominosPizza cursorToDominosPizza(Cursor cursor) {
        DominosPizza dominosPizza = new DominosPizza();
        dominosPizza.setId(cursor.getLong(0));
        dominosPizza.setAdresse(cursor.getString(1));
        dominosPizza.setNumeroTelephone(cursor.getString(2));
        return dominosPizza;
    }

}
