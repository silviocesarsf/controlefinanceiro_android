package com.example.controlefinanceiro.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.controlefinanceiro.controllers.ContaController;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "controlefinanceiro.db";
    private static final int DATABASE_VERSION = 1;

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Conta (id INTEGER PRIMARY KEY, Titulo TEXT NOT NULL, Valor FLOAT NOT NULL, Data DATETIME NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Conta");
        onCreate(db);
    }
}
