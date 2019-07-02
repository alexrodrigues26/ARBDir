package com.teoriagrafos.athena;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.Objects;

public class SelecionaOrientacao extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleciona_orientacao);

        // Pega os botões.
        Button btn1 = findViewById(R.id.btnOrientado);
        Button btn2 = findViewById(R.id.btnNaoOrientado);

        // Pega a toolbar e seta um texto.
        Toolbar toolbar = findViewById(R.id.toolbarSelecionaOrientacao);
        toolbar.setTitle(R.string.toolbar_selecionar_orientacao);

        // Coloca o evento clique nos botões.
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);

        // Colocar suporte de action bar na toolbar e coloca a seta home.
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    // Sobrescreve o método onClick(View v) para gerenciar o que ocorre quando um botão é clicado.
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnOrientado:
                openTab(true);
                break;

            case R.id.btnNaoOrientado:
                openTab(false);
                break;
        }
    }

    // Método para abrir a classe Tab e evitar repetição de código.
    private void openTab(boolean orientado){
        Intent intent = new Intent(this, Tab.class);
        intent.putExtra("orientado", orientado);
        startActivity(intent);
    }

    // Sobrescreve o método onOptionsItemSelected(MenuItem item) para quando clicar na seta finalizar a activity.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
