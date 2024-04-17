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

    public Boolean insertConta(String titulo, float valor, String dataVencimento, String dataCriacao, Integer CodigoStatus) {

        // Guia status
        // 1 - Pendente
        // 2 - Vencido
        // 3 - Pago
        SQLiteDatabase db = null;
        try {
            db = dbConta.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("Titulo", titulo);
            values.put("Valor", valor);
            values.put("DataVencimento", dataVencimento);
            values.put("DataCriacao", dataCriacao);
            values.put("CodigoStatus", CodigoStatus);
            db.insert("Conta", null, values);
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public ArrayList<HashMap<String, String>> querySelect(String parameter, String query) {

        SQLiteDatabase db = null;

        try {
            db = dbConta.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, parameter != null ? new String[]{parameter.toString()} : null);
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

    public Boolean updateConta(String[] param, String[] whereValue) {
        Boolean statusTransacao = false;
        try {
            SQLiteDatabase db = dbConta.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("Titulo", param[0]);
            values.put("Valor", param[1]);
            values.put("CodigoStatus", param[2]);
            int rowsAffected = db.update("Conta", values, "id = ?", whereValue);
            db.close();

            if (rowsAffected > 0) {
                statusTransacao = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return statusTransacao;
    }

    public Boolean deleteConta(String[] id) {
        Boolean statusTransacao = false;

        try {
            SQLiteDatabase db = dbConta.getWritableDatabase();
            Integer rowsAffected = db.delete("Conta", "id = ?", id);

            if(rowsAffected > 0) {
                statusTransacao = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        };

        return statusTransacao;
    }
}
