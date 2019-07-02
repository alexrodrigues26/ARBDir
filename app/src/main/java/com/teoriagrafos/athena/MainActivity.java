package com.teoriagrafos.athena;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Pega os botões e a toolbar na activity.
        Button btn1 = findViewById(R.id.btnComecar);
        Button btn2 = findViewById(R.id.btnSobre);
        Button btn3 = findViewById(R.id.btnAjuda);
        Toolbar toolbar = findViewById(R.id.toolbarMain);

        // Coloca o evento clique nos botões.
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);

        // Coloca um texto na toolbar.
        toolbar.setTitle(R.string.toolbar_main_activity);
    }

    // Sobrescreve o método onClick(View v) para gerenciar o que ocorre quando um botão é clicado.
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnComecar:
                abreNovaTela(SelecionaOrientacao.class);
                break;

            case R.id.btnSobre:
                abreNovaTela(Sobre.class);
                break;

            case R.id.btnAjuda:
                abreNovaTela(Ajuda.class);
                break;
        }
    }

    // Método para abrir outra activity.
    private void abreNovaTela(Class novaTela) {
        Intent intent = new Intent(this, novaTela);
        startActivity(intent);
    }
}