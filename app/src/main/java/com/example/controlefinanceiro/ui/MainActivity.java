package com.example.controlefinanceiro.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.controlefinanceiro.R;
import com.example.controlefinanceiro.controllers.ContaController;

public class MainActivity extends AppCompatActivity {
    FrameLayout btnAdd;
    private ContaController contaController = new ContaController(this);
    Boolean intentFromAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnAdd = findViewById(R.id.btnAddConta);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentToAddConta();
            }
        });

        // Verificando se veio da tela de adicionar contas

        if (getIntent() != null && getIntent().hasExtra("from_add")) {
            intentFromAdd = getIntent().getBooleanExtra("from_add", false);
        }

        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.black));
    }

    private void IntentToAddConta() {
        Intent intent = new Intent(this, ContaAdd.class);
        startActivity(intent);
    }
}