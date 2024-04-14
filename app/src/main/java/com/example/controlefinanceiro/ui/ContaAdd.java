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
import com.example.controlefinanceiro.controllers.ContaController;
import com.example.controlefinanceiro.utils.MoneyTextWatcher;

public class ContaAdd extends AppCompatActivity {
    FrameLayout btnBack;
    Button btnSave;
    EditText edtTitulo;
    EditText edtValor;
    EditText edtData;
    String tituloString = null;
    Float valorFloat = null;
    String dataString = null;
    ContaController contaController = new ContaController(this);


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

        edtValor.addTextChangedListener(new MoneyTextWatcher(edtValor));

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
                String textoValor = edtValor.getText().toString();

                try {
                    String valorSemR = textoValor.replaceAll("\\s+|R\\$", "");

                    valorSemR = valorSemR.replace(",", ".");

                    valorFloat = Float.parseFloat(valorSemR);
                } catch (NumberFormatException e) {

                }

                if (tituloString.isEmpty() || dataString.isEmpty() || valorFloat == null) {
                    Toast.makeText(ContaAdd.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean statusInsert = contaController.insertConta(tituloString, valorFloat, dataString);

                    if (statusInsert) {
                        IntentToMain();
                        Toast.makeText(ContaAdd.this, "Conta salva !", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void IntentToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("from_add", true);
        startActivity(intent);
        finish();
    }
}