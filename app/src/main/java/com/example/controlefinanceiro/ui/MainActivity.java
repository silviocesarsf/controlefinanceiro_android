package com.example.controlefinanceiro.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.controlefinanceiro.R;
import com.example.controlefinanceiro.controllers.ContaController;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    FrameLayout btnAdd;
    private ContaController contaController = new ContaController(this);

    ArrayList<HashMap<String, String>> contasSemFiltro = null;
    TextView tvSemContas;
    ImageView btnConfiguracoes;
    FrameLayout btnFiltrarPagas;
    FrameLayout btnFiltrarPendentes;
    FrameLayout btnFiltrarTudo;
    LinearLayout linearLayout;
    TextView tvExibicaoFiltro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        linearLayout = findViewById(R.id.linearCards);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnAdd = findViewById(R.id.btnAddConta);
        btnConfiguracoes = findViewById(R.id.btnConfiguracoes);
        btnFiltrarPagas = findViewById(R.id.btnContasPagas);
        btnFiltrarPendentes = findViewById(R.id.btnFiltrarPendentes);
        btnFiltrarTudo = findViewById(R.id.btnFiltrarTudo);
        tvExibicaoFiltro = findViewById(R.id.exibindoFiltro);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentToAddConta();
            }
        });
        btnConfiguracoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Configuration.class);
                startActivity(intent);
            }
        });
        btnFiltrarPagas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listarContasPagas();
            }
        });
        btnFiltrarPendentes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listarContasPendentes();
            }
        });
        btnFiltrarTudo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListarTodasContas();
            }
        });

        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.black));
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.main));

        ListarTodasContas();
    }

    private void IntentToAddConta() {
        Intent intent = new Intent(this, ContaAdd.class);
        startActivity(intent);
    }

    private void addCard(String valorContaArgs, String dataVencimentoArgs, String tituloContaArgs, Integer id) {
        View cardComponent = LayoutInflater.from(this).inflate(R.layout.card_component, null).findViewById(R.id.cardComponent);
        TextView tituloConta = cardComponent.findViewById(R.id.tituloConta);
        TextView valorConta = cardComponent.findViewById(R.id.valorConta);
        TextView dataVencimento = cardComponent.findViewById(R.id.dataVencimento);

        tituloConta.setText(tituloContaArgs);
        valorConta.setText("R$ " + valorContaArgs.replace(".", ","));
        dataVencimento.setText(dataVencimentoArgs);

        cardComponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DetalharConta.class);
                intent.putExtra("titulo", tituloContaArgs);
                intent.putExtra("valor", valorContaArgs);
                intent.putExtra("dataVencimento", dataVencimentoArgs);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        linearLayout.addView(cardComponent);
    }

    private void adicionarMensagemSemContas(String mensagem) {
        TextView textView = new TextView(this);
        textView.setText(mensagem);
        textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
        linearLayout.removeAllViews();
        linearLayout.addView(textView);
    }

    private void inserirLoader() {
        ProgressBar loader = new ProgressBar(MainActivity.this);
        loader.setForegroundGravity(View.TEXT_ALIGNMENT_CENTER);
        linearLayout.removeAllViews();
        linearLayout.addView(loader);
    }
    private void ListarTodasContas() {
        inserirLoader();
        tvExibicaoFiltro.setText("Todos");
        tvExibicaoFiltro.setTextColor(getResources().getColor(R.color.white));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    contasSemFiltro = contaController.querySelect(null, "SELECT * FROM Conta");

                    if (contasSemFiltro.isEmpty()) {
                        adicionarMensagemSemContas("Nenhuma Conta cadastrada");
                    } else {
                        linearLayout.removeAllViews();
                        contasSemFiltro.forEach((dados) -> {
                            addCard(dados.get("Valor"), dados.get("DataVencimento"), dados.get("Titulo"), Integer.valueOf(dados.get("id")));
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 1000);
    }
    private void listarContasPagas() {
        inserirLoader();
        tvExibicaoFiltro.setText("Pagos");
        tvExibicaoFiltro.setTextColor(getResources().getColor(R.color.green));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<HashMap<String, String>> retornoFiltradoPago = contaController.querySelect("3", "SELECT * FROM Conta WHERE CodigoStatus = ?");

                    if (retornoFiltradoPago.isEmpty()) {
                        adicionarMensagemSemContas("Não há contas pagas");
                    } else {
                        linearLayout.removeAllViews();
                        retornoFiltradoPago.forEach((dados) -> {
                            addCard(dados.get("Valor"), dados.get("DataVencimento"), dados.get("Titulo"), Integer.valueOf(dados.get("id")));
                        });
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Erro ao filtrar contas pagas: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, 1000);
    }
    private void listarContasPendentes() {
        inserirLoader();
        tvExibicaoFiltro.setText("Pendentes");
        tvExibicaoFiltro.setTextColor(getResources().getColor(R.color.main));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<HashMap<String, String>> retornoFiltradoPendente = contaController.querySelect("1", "SELECT * FROM Conta WHERE CodigoStatus = ?");

                    if (retornoFiltradoPendente.isEmpty()) {
                        adicionarMensagemSemContas("Não há contas pendentes");
                    } else {
                        linearLayout.removeAllViews();
                        retornoFiltradoPendente.forEach((dados) -> {
                            addCard(dados.get("Valor"), dados.get("DataVencimento"), dados.get("Titulo"), Integer.valueOf(dados.get("id")));
                        });
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Erro ao filtrar contas pendentes: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, 1000);
    }

};