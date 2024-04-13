package com.example.controlefinanceiro.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.controlefinanceiro.R;

public class AddContaScreen extends AppCompatActivity {
    FrameLayout btnBack;
    Button btnSave;
    EditText edtTitulo;
    EditText edtValor;
    EditText edtData;
    String tituloString = null;
    String valorString = null;
    String dataString = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_conta_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);
        edtTitulo = findViewById(R.id.edtTitulo);
        edtValor = findViewById(R.id.edtValor);
        edtData = findViewById(R.id.edtData);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentToMain();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tituloString = edtTitulo.getText().toString();
                dataString = edtData.getText().toString();
                valorString = edtValor.getText().toString();

                if(tituloString.isEmpty() || dataString.isEmpty() || valorString.isEmpty()) {
                    Toast.makeText(AddContaScreen.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void IntentToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}