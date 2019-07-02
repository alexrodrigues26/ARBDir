package com.teoriagrafos.athena;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.teoriagrafos.athena.adapter.ViewPagerAdapter;
import com.teoriagrafos.athena.interfaces.HelperFuncoes;
import com.teoriagrafos.athena.interfaces.HelperGrafo;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class Tab extends AppCompatActivity implements HelperGrafo, HelperFuncoes, Coloracao.HelperColoracao {

    // Variáveis para trabalhar na classe.
    private boolean orientado;
    private String[] funcoes;
    private GrafoFragment grafoFragment;
    private GrafoOrientadoFragment grafoOrientadoFragment;
    private FuncoesFragment funcoesFragment;
    private FuncoesOrientadoFragment funcoesOrientadoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        // Cria instancias dos fragments quando o onCreate for executado.
        grafoFragment = new GrafoFragment();
        grafoOrientadoFragment = new GrafoOrientadoFragment();
        funcoesFragment = new FuncoesFragment();
        funcoesOrientadoFragment = new FuncoesOrientadoFragment();

        // Preenche uma vetor de string com os dados do array que contem as funcoes.
        funcoes = getResources().getStringArray(R.array.funcoes);

        // Pega o valor que veio pela intent.
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
            orientado = bundle.getBoolean("orientado");

        // Pega os elementos que vão ser manipulados.
        Toolbar toolbar = findViewById(R.id.toolbarTab);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.viewPager);

        // Seta um texto na toolbar dependendo da orientação.
        if (orientado){
            toolbar.setTitle(R.string.toolbar_tab_orientado);
        } else {
            toolbar.setTitle(R.string.toolbar_tab_nao_orientado);
        }

        //  Coloca suporte de action bar na toolbar e coloca a seta home.
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Cria um adapter para o 'tabLayout'.
        setupViewPager(viewPager);

        tabLayout.setupWithViewPager(viewPager);
    }

    // Método para criar a view.
    private void setupViewPager(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        if (orientado) {
            adapter.AddFragment(grafoOrientadoFragment, "Grafo");
            adapter.AddFragment(funcoesOrientadoFragment, "funções");
        } else {
            adapter.AddFragment(grafoFragment, "Grafo");
            adapter.AddFragment(funcoesFragment, "funções");
        }

        viewPager.setAdapter(adapter);
    }

    // Sobrescreve o método onOptionsItemSelected(MenuItem item) para quando clicar na seta finalizar a activity.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void enviarInformacoes(boolean chamou, List<String> listNos, List<String> listArestas, List<String> listPeso) {
        // Se chamou == true (GrafoOrientadoFragment), se chamou == false (GrafoFragment).
        if (chamou){
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("listNos", (ArrayList<String>) listNos);
            bundle.putStringArrayList("listArestas", (ArrayList<String>) listArestas);
            bundle.putStringArrayList("listPeso", (ArrayList<String>) listPeso);
            funcoesOrientadoFragment.setArguments(bundle);
        } else {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("listNos", (ArrayList<String>) listNos);
            bundle.putStringArrayList("listArestas", (ArrayList<String>) listArestas);
            bundle.putStringArrayList("listPeso", (ArrayList<String>) listPeso);
            funcoesFragment.setArguments(bundle);
        }
    }

    @Override
    public void enviaColoracaoResp(boolean chamou, String resp) {

    }

    // Sobrescreve o método pegarInformacoesOrientado(...) para saber o que fazer quando o usuario seleciona uma funcionalidade.
    @Override
    public void pegarInformacoes(boolean chamou, String funcao) {
        // Se foi chamado pela FuncoesOrientadoFragment.
        if (chamou){
            if (grafoOrientadoFragment != null){
                // Se funcao == Informações Gerais.
                if (funcao.equals(funcoes[1])){
                    grafoOrientadoFragment.validaEnviar();
                    // Se funcao == DijkstraExecutor.
                } else if (funcao.equals(funcoes[2])){
                    grafoOrientadoFragment.validaEnviar();
                    // Se funcao == Coloração.
                } else if (funcao.equals(funcoes[3])){
                    grafoOrientadoFragment.executarColoracao();
                    grafoOrientadoFragment.validaEnviar();
                }else if (funcao.equals(funcoes[4])){
                    grafoOrientadoFragment.validaEnviar();
                }else if (funcao.equals(funcoes[5])){
                    grafoOrientadoFragment.validaEnviar();
                }
            }
            // Se foi chamado pela FuncoesFragment.
        } else {
            if (grafoFragment != null){
                // Se funcao == Informações Gerais.
                if (funcao.equals(funcoes[1])){
                    grafoFragment.validaEnviar();
                    // Se funcao == DijkstraExecutor.
                } else if (funcao.equals(funcoes[2])){
                    grafoFragment.validaEnviar();
                    // Se funcao == Coloração.
                } else if (funcao.equals(funcoes[3])){
                    grafoFragment.executarColoracao();
                    grafoFragment.validaEnviar();
                } else if (funcao.equals(funcoes[4])){
                    grafoFragment.validaEnviar();
                } else if (funcao.equals(funcoes[5])){
                    grafoFragment.validaEnviar();
                }
            }
        }
    }

    @Override
    public void pintarCaminhoMinimo(boolean chamou, String noOrigem, String noDestino){
        if (chamou){
            grafoOrientadoFragment.executarDijkstra(noOrigem, noDestino);
        } else {
            grafoFragment.executarDijkstra(noOrigem, noDestino);
        }
    }

    @Override
    public void mandaTexto(boolean chamou,String alteraResp) {
        if (chamou){
            funcoesOrientadoFragment.alteraResp(alteraResp);
        } else {
            funcoesFragment.alteraResp(alteraResp);
        }
    }

    @Override
    public void limpar(boolean chamou) {
        if (chamou){
            grafoOrientadoFragment.limparCorGrafo();
        } else {
            grafoFragment.limparCorGrafo();
        }
    }

    @Override
    public void executarBusca(boolean chamou, String algo, String noOrigem) {
        if (chamou){
            if (algo.equals("Profundo")){
                grafoOrientadoFragment.executaProfundidade(noOrigem);
            } else if (algo.equals("Largura")){
                grafoOrientadoFragment.executaLargura(noOrigem);
            }
        } else {
            if (algo.equals("Profundo")){
                grafoFragment.executaProfundidade(noOrigem);
            } else if (algo.equals("Largura")){
                grafoFragment.executaLargura(noOrigem);
            }
        }
    }
}