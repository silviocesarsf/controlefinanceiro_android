package com.example.controlefinanceiro.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.controlefinanceiro.R;
import com.example.controlefinanceiro.controllers.ContaController;
import com.example.controlefinanceiro.utils.MoneyTextWatcher;

public class DetalharConta extends AppCompatActivity {
    ImageView btnBack;
    TextView tvNomeConta;
    EditText edtEditarTitulo;
    EditText edtEditarValor;
    CheckBox checkPago;
    Boolean usuarioDefiniuComoPago = false;
    Button btnSalvarAlteracoes;
    Button btnExcluirConta;
    ContaController contaController = new ContaController(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalhar_conta);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.black));
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.main));

        btnBack = findViewById(R.id.btnBack);
        tvNomeConta = findViewById(R.id.nomeConta);
        edtEditarTitulo = findViewById(R.id.edtTituloEditar);
        edtEditarValor = findViewById(R.id.edtValorEditar);
        checkPago = findViewById(R.id.checkPago);
        btnSalvarAlteracoes = findViewById(R.id.btnSalvarAlteracoes);
        btnExcluirConta = findViewById(R.id.btnExcluirConta);

        edtEditarValor.addTextChangedListener(new MoneyTextWatcher(edtEditarValor));

        if (getIntent() != null && getIntent().hasExtra("titulo")) {
            tvNomeConta.setText(getIntent().getStringExtra("titulo"));
            edtEditarTitulo.setText(getIntent().getStringExtra("titulo"));
            edtEditarValor.setText(getIntent().getStringExtra("valor"));
        } else {
            startActivity(new Intent(DetalharConta.this, MainActivity.class));
            Log.e("Erro", "Ocorreu um erro ao recuperar informacoes da conta");
            Toast.makeText(this, "Erro ao recuperar informações", Toast.LENGTH_SHORT).show();
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentToMain();
            }
        });
        checkPago.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    usuarioDefiniuComoPago = true;
                } else {
                    usuarioDefiniuComoPago = false;
                }
            }
        });

        btnSalvarAlteracoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usuarioDefiniuComoPago) {
                    Boolean retorno = contaController.updateConta(new String[]{"Teste atualizou", "19,20", "3"}, new String[]{String.valueOf(getIntent().getIntExtra("id", 0))});

                    if (retorno) IntentToMain();
                }
            }
        });

        btnExcluirConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean retorno = contaController.deleteConta(new String[]{String.valueOf(getIntent().getIntExtra("id", 0))});

                if (retorno) IntentToMain();
            }
        });
    }

    private void IntentToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}