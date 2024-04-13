package com.example.controlefinanceiro.controllers;

import android.content.Context;

import com.example.controlefinanceiro.data.Database;

public class ContaController {
    private Database dbConta;
    public ContaController(Context context) {
        dbConta = new Database(context);
    }
}
