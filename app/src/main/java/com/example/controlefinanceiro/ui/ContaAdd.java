package com.example.controlefinanceiro.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    Date dataAtual = new Date();
                    Boolean statusInsert = contaController.insertConta(tituloString, valorFloat, dataString, formatter.format(dataAtual).toString(), 1);

                    if (statusInsert) {
                        IntentToMain();
                        Toast.makeText(ContaAdd.this, "Conta salva !", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        edtData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.main));
    }

    private void IntentToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("from_add", true);
        startActivity(intent);
        finish();
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                Calendar dataSelecionada = Calendar.getInstance();
                dataSelecionada.set(year, month, dayOfMonth);

                SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
                String dataFormatada = formatador.format(dataSelecionada.getTime());

                edtData.setText(dataFormatada);
            }
        }, ano, mes, dia);

        datePickerDialog.show();
    }
}