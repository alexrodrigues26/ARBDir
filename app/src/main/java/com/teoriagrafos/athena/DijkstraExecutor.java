package com.teoriagrafos.athena;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.graphstream.algorithm.Dijkstra;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DijkstraExecutor extends Fragment {

    private Graph g;
    private List<String> listNos = new ArrayList<>(), listArestas = new ArrayList<>(), listPeso = new ArrayList<>();
    private String noOrigem, noDestino;
    private boolean computa, chamou;
    private StringBuilder s = new StringBuilder();
    private Coloracao.HelperColoracao listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_dijkstra, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Cria um novo Grafo.
        g = new MultiGraph("AthenaGraph5");

        // Chama o método para pegar os dados que vieram via Bundle.
        getParametros();

        if (computa) {
            // Chama o método para re-criar o grafo.
            recriarGrafo();

            if (g.getNodeCount() != 0) {
                // Chama o método para mostrar a execução.
                mostraExecucao();
            }
        }
    }

    // Pega parâmetros que chegaram via Bundle.
    private void getParametros(){
        Bundle bundle = getArguments();
        if (bundle != null){
            listNos = bundle.getStringArrayList("listNos");
            listArestas = bundle.getStringArrayList("listArestas");
            listPeso = bundle.getStringArrayList("listPeso");
            noOrigem = bundle.getString("noOrigem");
            noDestino = bundle.getString("noDestino");
            chamou = bundle.getBoolean("chamou");
            computa = bundle.getBoolean("computa");
        }
    }

    // Método para re-criar o grafo.
    private void recriarGrafo(){
        // Re-cria os nos.
        for (int i = 0; i < listNos.size(); i++){
            g.addNode(listNos.get(i));
        }

        // Re-cria as arestas com peso.
        for (int i = 0; i < listArestas.size(); i++){
            String nomeAresta = listArestas.get(i);
            String noFrom = String.valueOf(listArestas.get(i).charAt(0));
            String noTo = String.valueOf(listArestas.get(i).charAt(1));
            if (chamou) {
                g.addEdge(nomeAresta, noFrom, noTo, true);
            } else {
                g.addEdge(nomeAresta, noFrom, noTo, false);
            }
            g.getEdge(nomeAresta).setAttribute("length", listPeso.get(i));
        }
    }

    private void mostraExecucao(){
        Dijkstra d = new Dijkstra(Dijkstra.Element.EDGE, "result", "length");
        d.init(g);
        d.setSource(g.getNode(noOrigem));
        d.compute();

        int pesoTotal = (int) d.getPathLength(g.getNode(noDestino));
        String caminho = String.valueOf(d.getPath(g.getNode(noDestino)));

        s.append("O caminho minimo do nó  origem: ").append(noOrigem)
                .append(" para o nó destino: ").append(noDestino)
                .append(" tem peso total de ").append(pesoTotal)
                .append("\n\n Caminho percorido: ")
                .append(caminho);

        s.append("\n\n<-- <-- Se você voltar para a página '\'GRAFO'\' verá o caminho mínimo.");

        // Retorna a string de execução.
        if (listener != null)
            listener.mandaTexto(chamou, String.valueOf(s));
    }

    private String imprimeMatrix(String[][] matrix){
        StringBuilder s = new StringBuilder();

        for (int i = 0; i < matrix.length; i++){
            for (int j = 0; j < matrix.length; j++){
                s.append(matrix[i][j]).append(" ");
            }
            s.append("\n");
        }

        return s.toString();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Coloracao.HelperColoracao) {
            listener = (Coloracao.HelperColoracao) context;
        } else {
            throw new RuntimeException(context.toString() + getString(R.string.helper_coloracao));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
