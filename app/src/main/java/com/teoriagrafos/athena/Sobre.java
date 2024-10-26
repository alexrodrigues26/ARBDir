package com.teoriagrafos.athena;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.teoriagrafos.athena.adapter.ExpandableAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Sobre extends AppCompatActivity {

    // Variáveis para trabalhar na classe.
    private List<String> listGroup;
    private HashMap<String, List<String>> listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);

        // Pega a toolbar e seta um texto.
        Toolbar toolbar = findViewById(R.id.toolbarSobre);
        toolbar.setTitle(R.string.toolbar_sobre);

        // Colocar suporte de action bar na toolbar e coloca a seta home.
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        construirLista();

        // Cria uma instância de ExpandableListView e coloca um adapter.
        final ExpandableListView expandableListView = findViewById(R.id.sobreExpandableListView);
        expandableListView.setAdapter(new ExpandableAdapter(this, listGroup, listData));

        // Coloca os itens do grupo expansíveis.
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previosGroup = -1;
            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previosGroup){
                    expandableListView.collapseGroup(previosGroup);
                }
                previosGroup = groupPosition;
            }
        });

        // Muda o item do grupo quando clicado.
        expandableListView.setGroupIndicator(ContextCompat.getDrawable(this, R.drawable.icon_group));
    }

    // Cria um método para construir a lista.
    private void construirLista() {
        listGroup = new ArrayList<>();
        listData = new HashMap<>();

        // Cria os grupos com as informações.
        listGroup.add(getString(R.string.txt_tg));
        listGroup.add(getString(R.string.txt_arbdir));
        listGroup.add(getString(R.string.txt_autores));

        // Cria os filhos dos grupos.
        List<String> auxList = new ArrayList<>();
        auxList.add(getString(R.string.txt_tg_child));
        listData.put(listGroup.get(0), auxList);

        auxList = new ArrayList<>();
        auxList.add(getString(R.string.txt_arbdir_child));
        listData.put(listGroup.get(1), auxList);

        auxList = new ArrayList<>();
        auxList.add(getString(R.string.txt_autores_child_1));
        listData.put(listGroup.get(2), auxList);
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