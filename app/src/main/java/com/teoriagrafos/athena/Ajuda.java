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

public class Ajuda extends AppCompatActivity {

    // Variáveis para trabalhar na classe.
    private List<String> listGroup;
    private HashMap<String, List<String>> listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajuda);

        // Pega a toolbar e seta um texto.
        Toolbar toolbar = findViewById(R.id.toolbarAjuda);
        toolbar.setTitle(R.string.toolbar_ajuda);

        // Colocar suporte de action bar na toolbar e coloca a seta home.
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        construirLista();

        // Cria uma instância de ExpandableListView e coloca um adapter.
        final ExpandableListView expandableListView = findViewById(R.id.ajudaExpandableListView);
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
        listGroup.add(getString(R.string.como_posicionar_grao));
        listGroup.add(getString(R.string.como_add_no));
        listGroup.add(getString(R.string.como_add_aresta));
        listGroup.add(getString(R.string.como_deletar_elemento));
        listGroup.add(getString(R.string.como_descolorir));
        listGroup.add(getString(R.string.como_informacoes_gerais));
        listGroup.add(getString(R.string.algo_disponivel));

        // Cria os filhos dos grupos.
        List<String> auxList = new ArrayList<>();
        auxList.add(getString(R.string.como_posicionar_grao_child));
        listData.put(listGroup.get(0), auxList);

        auxList = new ArrayList<>();
        auxList.add(getString(R.string.como_add_no_child));
        listData.put(listGroup.get(1), auxList);

        auxList = new ArrayList<>();
        auxList.add(getString(R.string.como_add_aresta_child));
        listData.put(listGroup.get(2), auxList);

        auxList = new ArrayList<>();
        auxList.add(getString(R.string.como_deletar_elemento_child));
        listData.put(listGroup.get(3), auxList);

        auxList = new ArrayList<>();
        auxList.add(getString(R.string.como_descolorir_child));
        listData.put(listGroup.get(4), auxList);

        auxList = new ArrayList<>();
        auxList.add(getString(R.string.como_informacoes_gerais_child));
        listData.put(listGroup.get(5), auxList);

        auxList = new ArrayList<>();
        auxList.add(getString(R.string.algo_disponivel_child_1));
        auxList.add(getString(R.string.algo_disponivel_child_2));
        auxList.add(getString(R.string.algo_disponivel_child_3));
        auxList.add(getString(R.string.algo_disponivel_child_4));
        listData.put(listGroup.get(6), auxList);
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
