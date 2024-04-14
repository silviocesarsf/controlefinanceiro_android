package com.example.controlefinanceiro.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.controlefinanceiro.data.ContaDB;

import java.util.ArrayList;
import java.util.HashMap;

public class ContaController {
    private ContaDB dbConta;

    public ContaController(Context context) {
        dbConta = new ContaDB(context);
    }

    public Boolean insertConta(String titulo, float valor, String data) {

        SQLiteDatabase db = null;
        try {
            db = dbConta.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("Titulo", titulo);
            values.put("Valor", valor);
            values.put("DataVencimento", data);
            db.insert("Conta", null, values);
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            if(db != null) {
                db.close();
            }
        }
    }

    public ArrayList<HashMap<String, String>> querySelect(String parameter, String query) {

        SQLiteDatabase db = null;

        try {
            db = dbConta.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, new String[]{parameter.toString()});
            ArrayList<HashMap<String, String>> resultados = new ArrayList<>();

            while (cursor.moveToNext()) {
                HashMap<String, String> row = new HashMap<>();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    String columnName = cursor.getColumnName(i);
                    String columnValue = cursor.getString(i);
                    row.put(columnName, columnValue);
                }
                resultados.add(row);
            }

            cursor.close();
            db.close();
            return resultados;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Boolean dropTable() {
        Boolean status = false;
        SQLiteDatabase db = null;
        try {
            db = dbConta.getWritableDatabase();
            db.execSQL("DROP TABLE IF EXISTS Conta");
            status = true;
        } catch (Exception e) {
            e.printStackTrace();
            status = false;
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return status;
    }

}
