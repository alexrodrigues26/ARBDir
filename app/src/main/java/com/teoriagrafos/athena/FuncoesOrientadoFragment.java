package com.teoriagrafos.athena;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.teoriagrafos.athena.interfaces.HelperFuncoes;

import java.util.ArrayList;
import java.util.List;

public class FuncoesOrientadoFragment extends Fragment implements DialogSelecionaNo.HelperSelecionaNo,DialogSelecionaNos.HelperSelecionaNo {

    // Variáveis para trabalhar na classe.
    private Spinner spinner;
    private ImageButton btnExecutar;
    private FrameLayout frame1;
    private TextView tvResp1, tvResp2;
    private List<String> listNos, listArestas, listPeso;
    private FragmentManager fm;
    private String itemSelecionado, noOrigem, noDestino;
    private boolean noSelecionado;
    private String[] funcoes;
    private MostraInfo mostraInfo;
    private DijkstraExecutor dijkstraExecutor;
    private Coloracao coloracao;
    private Profundidade profundidade;
    private Largura largura;
    private HelperFuncoes listener;

    public FuncoesOrientadoFragment() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_funcoes, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Pega os elementos para serem trabalhados se getView() não for nulo.
        if (getView() != null) {
            spinner = getView().findViewById(R.id.spFuncoes);
            btnExecutar = getView().findViewById(R.id.btnExecutar);
            frame1 = getView().findViewById(R.id.frameLayout1);
            tvResp1 = getView().findViewById(R.id.tvResp1);
            tvResp2 = getView().findViewById(R.id.tvResp2);
            // Preenche uma vetor de string com os dados do array que contem as funcoes.
            funcoes = getResources().getStringArray(R.array.funcoes);
        }

        // Cria um adapter se activity nao nula e preenche ele com às funcionalidades e depois seta no spinner.
        if (getActivity() != null){
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,
                    funcoes);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinner.setAdapter(arrayAdapter);
        }

        spinner.setOnItemSelectedListener(selectedListener);
        btnExecutar.setOnClickListener(btnExecutarClicked);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.limpa_grafo_cor, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.limpar){
            if (listener != null)
                listener.limpar(true);
        }
        return super.onOptionsItemSelected(item);
    }

    // Método de OnItemSelectedListener do spinner.
    private Spinner.OnItemSelectedListener selectedListener =
            new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    itemSelecionado = parent.getItemAtPosition(position).toString();
                    // Se refere a "***" no array.
                    if (itemSelecionado.equals(funcoes[0])){
                        tvResp1.setVisibility(View.VISIBLE);
                        tvResp2.setVisibility(View.GONE);
                        frame1.setVisibility(View.GONE);
                        tvResp1.setText(getString(R.string.escolha_funcionalidade));
                        // Se refere a "Informações Gerais" no array.
                    } else if (itemSelecionado.equals(funcoes[1])){
                        tvResp1.setVisibility(View.VISIBLE);
                        tvResp2.setVisibility(View.GONE);
                        frame1.setVisibility(View.GONE);
                        tvResp1.setText(getString(R.string.info_grafo));
                        // Se refere a "DijkstraExecutor" no array.
                    }else if (itemSelecionado.equals(funcoes[2])){
                        tvResp1.setVisibility(View.VISIBLE);
                        tvResp2.setVisibility(View.GONE);
                        frame1.setVisibility(View.GONE);
                        posicionaTela(funcoes[2]);
                        tvResp1.setText(getString(R.string.disjktra));
                        // Se refere a "Coloração" no array.
                    }else if (itemSelecionado.equals(funcoes[3])){
                        tvResp1.setVisibility(View.VISIBLE);
                        tvResp2.setVisibility(View.GONE);
                        frame1.setVisibility(View.GONE);
                        posicionaTela(funcoes[3]);
                        tvResp1.setText(getString(R.string.coloracao));
                        // Se refere a "Busca em profundidade" no array.
                    }else if (itemSelecionado.equals(funcoes[4])) {
                        tvResp1.setVisibility(View.VISIBLE);
                        tvResp2.setVisibility(View.GONE);
                        frame1.setVisibility(View.GONE);
                        posicionaTela(funcoes[4]);
                        tvResp1.setText(getString(R.string.busca_profundidade));
                        // Se refere a "Busca em largura" no array.
                    }else if (itemSelecionado.equals(funcoes[5])) {
                        tvResp1.setVisibility(View.VISIBLE);
                        tvResp2.setVisibility(View.GONE);
                        frame1.setVisibility(View.GONE);
                        posicionaTela(funcoes[5]);
                        tvResp1.setText(getString(R.string.busca_largura));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            };

    // Método de OnClickListener do ImageButton btnExecutar.
    private ImageButton.OnClickListener btnExecutarClicked =
            v -> {
                // Se refere a "***" no array.
                if (itemSelecionado.equals(funcoes[0])){
                    chamaToast(getString(R.string.escolha_funcionalidade));
                    // Se refere a "Informações Gerais" no array.
                } else if (itemSelecionado.equals(funcoes[1])){
                    validarPedido(itemSelecionado);
                    getParametros();
                    showInfo(itemSelecionado);
                    // Se refere a "DijkstraExecutor" no array.
                } else if (itemSelecionado.equals(funcoes[2])){
                    validarPedido(itemSelecionado);
                    getParametros();
                    if (!listNos.isEmpty()){
                        openSelecionaNos();
                    } else {
                        chamaToast(getString(R.string.grafo_vazio));
                    }
                    // Se refere a "Coloração" no array.
                } else if (itemSelecionado.equals(funcoes[3])){
                    validarPedido(itemSelecionado);
                    getParametros();
                    if (!listNos.isEmpty()){
                        tvResp1.setVisibility(View.GONE);
                        tvResp2.setVisibility(View.VISIBLE);
                        showInfo(itemSelecionado);
                    } else {
                        chamaToast(getString(R.string.grafo_vazio));
                    }
                    // Se refere a "Busca em profundidade" no array.
                } else if (itemSelecionado.equals(funcoes[4])){
                    validarPedido(itemSelecionado);
                    getParametros();
                    if (!listNos.isEmpty()){
                        openSelecionaNo();
                    } else {
                        chamaToast(getString(R.string.grafo_vazio));
                    }
                    // Se refere a "Busca em largura" no array.
                } else if (itemSelecionado.equals(funcoes[5])) {
                    validarPedido(itemSelecionado);
                    getParametros();
                    if (!listNos.isEmpty()) {
                        openSelecionaNo();
                    } else {
                        chamaToast(getString(R.string.grafo_vazio));
                    }
                }
            };

    // Método que chama um Toast para facilitar o uso do mesmo.
    private void chamaToast(String mensagem){
        Toast.makeText(getContext(), mensagem, Toast.LENGTH_SHORT).show();
    }

    // Verifica se listner nao é nulo antes de chamar ele.
    private void validarPedido(String funcao){
        if (getActivity() != null)
            listener.pegarInformacoes(true, funcao);
    }

    // Pega parâmetros que chegaram de pegarInformacoes.
    private void getParametros(){
        listNos = new ArrayList<>(); listArestas = new ArrayList<>(); listPeso = new ArrayList<>();
        Bundle bundle = getArguments();
        if (bundle != null){
            listNos = bundle.getStringArrayList("listNos");
            listArestas = bundle.getStringArrayList("listArestas");
            listPeso = bundle.getStringArrayList("listPeso");
        }
    }

    // Método para mandar os dados para activity que vai exibir eles.
    private void showInfo(String tela){
        if (!listNos.isEmpty()){
            // Se refere a "Informações Gerais" no array.
            if (tela.equals(funcoes[1])){
                Bundle bundle = new Bundle();
                mostraInfo = new MostraInfo();
                tvResp1.setVisibility(View.GONE);
                frame1.setVisibility(View.VISIBLE);
                bundle.putStringArrayList("listNos", (ArrayList<String>) listNos);
                bundle.putStringArrayList("listArestas", (ArrayList<String>) listArestas);
                bundle.putStringArrayList("listPeso", (ArrayList<String>) listPeso);
                bundle.putBoolean("chamou", true);
                posicionaTela(tela);
                mostraInfo.setArguments(bundle);
                // Se refere a "Dijkstra" no array.
            } else if (tela.equals(funcoes[2])){
                Bundle bundle = new Bundle();
                dijkstraExecutor = new DijkstraExecutor();
                bundle.putStringArrayList("listNos", (ArrayList<String>) listNos);
                bundle.putStringArrayList("listArestas", (ArrayList<String>) listArestas);
                bundle.putStringArrayList("listPeso", (ArrayList<String>) listPeso);
                bundle.putString("noOrigem", noOrigem);
                bundle.putString("noDestino", noDestino);
                bundle.putBoolean("chamou", true);
                bundle.putBoolean("computa", true);
                posicionaTela(tela);
                dijkstraExecutor.setArguments(bundle);
                // Se refere a "Coloração" no array.
            } else if (tela.equals(funcoes[3])){
                Bundle bundle = new Bundle();
                coloracao = new Coloracao();
                bundle.putStringArrayList("listNos", (ArrayList<String>) listNos);
                bundle.putStringArrayList("listArestas", (ArrayList<String>) listArestas);
                bundle.putStringArrayList("listPeso", (ArrayList<String>) listPeso);
                bundle.putBoolean("chamou", true);
                bundle.putBoolean("colore", true);
                posicionaTela(tela);
                coloracao.setArguments(bundle);
                // Se refere a "Busca em profundidade" no array.
            } else if (tela.equals(funcoes[4])){
                Bundle bundle = new Bundle();
                profundidade = new Profundidade();
                bundle.putStringArrayList("listNos", (ArrayList<String>) listNos);
                bundle.putStringArrayList("listArestas", (ArrayList<String>) listArestas);
                bundle.putStringArrayList("listPeso", (ArrayList<String>) listPeso);
                bundle.putString("noOrigem", noOrigem);
                bundle.putBoolean("chamou", true);
                bundle.putBoolean("computa", true);
                posicionaTela(tela);
                profundidade.setArguments(bundle);
                // Se refere a "Busca em largura" no array.
            } else if (tela.equals(funcoes[5])){
                Bundle bundle = new Bundle();
                largura = new Largura();
                bundle.putStringArrayList("listNos", (ArrayList<String>) listNos);
                bundle.putStringArrayList("listArestas", (ArrayList<String>) listArestas);
                bundle.putStringArrayList("listPeso", (ArrayList<String>) listPeso);
                bundle.putString("noOrigem", noOrigem);
                bundle.putBoolean("chamou", true);
                bundle.putBoolean("computa", true);
                posicionaTela(tela);
                largura.setArguments(bundle);
            }
        } else {
            chamaToast(getString(R.string.grafo_vazio));
        }
    }

    // Método para colocar o layout para mostar as informações.
    private void posicionaTela(String tela){
        if (getActivity() != null)
            fm = getActivity().getSupportFragmentManager();
        // Se refere a "Informações Gerais" no array.
        if (tela.equals(funcoes[1])){
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.frameLayout1, mostraInfo, "MostraInfo");
            ft.commit();
            // Se refere a "Dijkstra" no array.
        }else if (tela.equals(funcoes[2])){
            dijkstraExecutor = new DijkstraExecutor();
            frame1.setVisibility(View.VISIBLE);
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.frameLayout1, dijkstraExecutor, "DijkstraExecutor");
            ft.commit();
            // Se refere a "Coloração" no array.
        }else if (tela.equals(funcoes[3])){
            coloracao = new Coloracao();
            frame1.setVisibility(View.VISIBLE);
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.frameLayout1, coloracao, "Coloracao");
            ft.commit();
            // Se refere a "Busca em profundidade" no array.
        }else if (tela.equals(funcoes[4])){
            profundidade = new Profundidade();
            frame1.setVisibility(View.VISIBLE);
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.frameLayout1, profundidade, "Profundidade");
            ft.commit();
            // Se refere a "Busca em largura" no array.
        }else if (tela.equals(funcoes[5])){
            largura = new Largura();
            frame1.setVisibility(View.VISIBLE);
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.frameLayout1, largura, "Largura");
            ft.commit();
        }
    }

    // Método para alterar o tvResp2.
    public void alteraResp(String novaResp){
        tvResp2.setText(novaResp);
    }

    // Método para abrir o Dialog para selecionar os nós.
    private void openSelecionaNos(){
        DialogSelecionaNos dialogSelecionaNos = new DialogSelecionaNos();
        Bundle bundle = new Bundle();

        bundle.putStringArrayList("listNos", (ArrayList<String>) listNos);
        dialogSelecionaNos.setArguments(bundle);
        dialogSelecionaNos.setTargetFragment(this, 1);
        assert getActivity() != null;
        dialogSelecionaNos.show(getActivity().getSupportFragmentManager(), "Selecionar Nós");
    }

    // Método para abrir o Dialog para selecionar o nó.
    private void openSelecionaNo(){
        DialogSelecionaNo dialogSelecionaNo = new DialogSelecionaNo();
        Bundle bundle = new Bundle();

        bundle.putStringArrayList("listNos", (ArrayList<String>) listNos);
        if (itemSelecionado.equals(funcoes[4])){
            bundle.putString("algo", "Profundo");
        } else {
            bundle.putString("algo", "Largura");
        }
        dialogSelecionaNo.setArguments(bundle);
        dialogSelecionaNo.setTargetFragment(this, 1);
        assert getActivity() != null;
        dialogSelecionaNo.show(getActivity().getSupportFragmentManager(), "Selecionar Nó");
    }

    // Método para pegar os nós.
    @Override
    public void applySelecionaNos(String noOrigem, String noDestino) {
        this.noOrigem = noOrigem;
        this.noDestino = noDestino;
        if (listener != null)
            listener.pintarCaminhoMinimo(true, noOrigem, noDestino);
        tvResp1.setVisibility(View.GONE);
        tvResp2.setVisibility(View.VISIBLE);
        showInfo(itemSelecionado);
    }

    // Método para pegar o nó.
    @Override
    public void applySelecionaNo(String algo, String noOrigem) {
        this.noOrigem = noOrigem;
        if (listener != null)
            listener.executarBusca(true, algo, noOrigem);
        tvResp1.setVisibility(View.GONE);
        tvResp2.setVisibility(View.VISIBLE);
        showInfo(itemSelecionado);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HelperFuncoes) {
            listener = (HelperFuncoes) context;
        } else {
            throw new RuntimeException(context.toString()+ getString(R.string.helper_funcoes));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
