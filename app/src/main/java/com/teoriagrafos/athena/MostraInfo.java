package com.teoriagrafos.athena;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.graphstream.algorithm.ConnectedComponents;
import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MostraInfo extends Fragment {

    // Variáveis para trabalhar na classe.
    private Graph g;
    private TextView linha1_resp, linha2_resp, linha3_resp, linha4_resp, linha6_resp, linha7_resp;
    private List<String> listNos = new ArrayList<>(), listArestas = new ArrayList<>(), listPeso = new ArrayList<>();
    private boolean chamou;
    private StringBuilder showNo, showAresta;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_mostra_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Cria um novo Grafo.
        g = new MultiGraph("AthenaGraph2");

        // Pega os TextView a serem editados.
        if (getView() != null){
            linha1_resp = getView().findViewById(R.id.linha1_resp);
            linha2_resp = getView().findViewById(R.id.linha2_resp);
            linha3_resp = getView().findViewById(R.id.linha3_resp);
            linha4_resp = getView().findViewById(R.id.linha4_resp);
            linha6_resp = getView().findViewById(R.id.linha6_resp);
            linha7_resp = getView().findViewById(R.id.linha7_resp);
        }

        // Chama o método para pegar os dados que vieram via Bundle.
        getParametros();

        // Chama o método para re-criar o grafo.
        recriarGrafo();

        // Chama método para preencher os campos.
        preencheCampos();
    }

    // Pega parâmetros que chegaram via Bundle.
    private void getParametros(){
        Bundle bundle = getArguments();
        if (bundle != null){
            listNos = bundle.getStringArrayList("listNos");
            listArestas = bundle.getStringArrayList("listArestas");
            listPeso = bundle.getStringArrayList("listPeso");
            chamou = bundle.getBoolean("chamou");
        }
    }

    // método para re-criar o grafo.
    private void recriarGrafo(){
        // Re-cria os nos.
        showNo = new StringBuilder();
        showNo.append("V={ ");
        for (int i = 0; i < listNos.size(); i++){
            g.addNode(listNos.get(i));
            showNo.append(listNos.get(i));
            if (i+1 < listNos.size()){
                showNo.append(",");
            }
        }
        showNo.append(" }");

        // Re-cria as arestas com peso.
        showAresta = new StringBuilder();
        showAresta.append("E={ ");
        for (int i = 0; i < listArestas.size(); i++){
            String nomeAresta = listArestas.get(i);
            String noFrom = String.valueOf(listArestas.get(i).charAt(0));
            String noTo = String.valueOf(listArestas.get(i).charAt(1));
            if (chamou) {
                showAresta.append("(").append(noFrom).append(",").append(noTo).append(")");
            } else {
                showAresta.append("{").append(noFrom).append(",").append(noTo).append("}");
            }
            g.addEdge(nomeAresta, noFrom, noTo, true);
            g.getEdge(nomeAresta).setAttribute("length", listPeso.get(i));
            if (i+1 < listArestas.size()){
                showAresta.append(",");
            }
        }
        showAresta.append(" }");
    }

    // método para preenche campos de resposta do layout.
    private void preencheCampos(){
        if (chamou) {
            linha1_resp.setText(getText(R.string.des_orientacao_1_resp));
            linha2_resp.setText(isConexoDirecionado());
        } else {
            linha1_resp.setText(getText(R.string.des_orientacao_2_resp));
            linha2_resp.setText(isConexo());
        }
        linha3_resp.setText(String.valueOf(listNos.size()));
        linha4_resp.setText(String.valueOf(listArestas.size()));
        linha6_resp.setText(showNo);
        linha7_resp.setText(showAresta);
    }

    private String isConexo(){
        String conexo;
        ConnectedComponents cc = new ConnectedComponents();
        cc.init(g);
        cc.compute();

        if (cc.getConnectedComponentsCount() != 1){
            conexo = getString(R.string.desconexo);
        } else {
            conexo = getString(R.string.conexo);
        }
        return conexo;
    }

    private String isConexoDirecionado() {
        String conexo;
        ConnectedComponents cc = new ConnectedComponents();
        cc.init(g);
        cc.compute();

        if (cc.getConnectedComponentsCount() != 1) {
            conexo = getString(R.string.c0);
        } else {
            int qtdNos = g.getNodeCount();
            int cont = 0, contTotal = 0;
            int qtdArestasPercoridas = qtdNos * qtdNos;
            int[] vet = new int[qtdArestasPercoridas];

            if (qtdNos >= 2) {
                for (int i = 0; i < qtdArestasPercoridas; i++) {
                    vet[i] = 0;
                }

                for (int i = 0; i < qtdNos; i++) {
                    Dijkstra d = new Dijkstra(Dijkstra.Element.EDGE, "result", "length");
                    d.init(g);
                    d.setSource(g.getNode(i));
                    d.compute();

                    for (Node node : g) {
                        double caminho = d.getPathLength(node);
                        if (!Double.isInfinite(caminho) && caminho >= 0.0)
                            vet[cont] = 1;
                        cont++;
                    }
                }

                for (int i = 0; i < qtdArestasPercoridas; i++) {
                    contTotal += vet[i];
                }

                if (contTotal == qtdArestasPercoridas) {
                    conexo = "C3";
                } else {
                    conexo = "C2";
                }
            } else {
                conexo = "C3";
            }
        }
        return conexo;
    }
}
