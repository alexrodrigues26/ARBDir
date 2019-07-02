package com.teoriagrafos.athena;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.graphstream.algorithm.ConnectedComponents;
import org.graphstream.algorithm.coloring.WelshPowell;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Coloracao extends Fragment {

    private Graph g;
    private TextView linha1, linha2, linha3, linha4, linha5, linha6, linha7, linha8, linha9, linha10, linha11,
            linha12, linha13, linha14, linha15;
    private List<String> listNos = new ArrayList<>(), listArestas = new ArrayList<>(), listPeso = new ArrayList<>();
    private boolean colore, chamou;
    private int matrix[][];
    private  StringBuilder s = new StringBuilder();
    private HelperColoracao listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_coloracao, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Cria um novo Grafo.
        g = new MultiGraph("AthenaGraph4");

        // Pega os TextView a serem editados.
        if (getView() != null){
            linha1 = getView().findViewById(R.id.coloracao_l1);
            linha2 = getView().findViewById(R.id.coloracao_l2);
            linha3 = getView().findViewById(R.id.coloracao_l3);
            linha4 = getView().findViewById(R.id.coloracao_l4);
            linha5 = getView().findViewById(R.id.coloracao_l5);
            linha6 = getView().findViewById(R.id.coloracao_l6);
            linha7 = getView().findViewById(R.id.coloracao_l7);
            linha8 = getView().findViewById(R.id.coloracao_l8);
            linha9 = getView().findViewById(R.id.coloracao_l9);
            linha10 = getView().findViewById(R.id.coloracao_l10);
            linha11 = getView().findViewById(R.id.coloracao_l11);
            linha12 = getView().findViewById(R.id.coloracao_l12);
            linha13 = getView().findViewById(R.id.coloracao_l13);
            linha14 = getView().findViewById(R.id.coloracao_l14);
            linha15 = getView().findViewById(R.id.coloracao_l15);
        }

        // Chama o método para pegar os dados que vieram via Bundle.
        getParametros();

        if (colore) {
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
            chamou = bundle.getBoolean("chamou");
            colore = bundle.getBoolean("colore");
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

    // Método para montar matriz de adjacencia.
    private void montaMatriz(){
        int n = g.getNodeCount();
        matrix = new int[n][n];
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                matrix[i][j] = (g.getNode(i).hasEdgeBetween(j) ? 1 : 0);
            }
        }
    }

    // método para colorir o algoritmo.
    private void colorirAlgo(){
        // Linhas 1 a 5.
        linha1.setBackgroundColor(Color.RED);
        linha2.setBackgroundColor(Color.RED);
        linha3.setBackgroundColor(Color.RED);
        linha4.setBackgroundColor(Color.RED);
        linha5.setBackgroundColor(Color.RED);

        // Linhas 6 e 14.
        linha6.setBackgroundColor(Color.YELLOW);
        linha14.setBackgroundColor(Color.YELLOW);

        // Linhas 7 e 12.
        linha7.setBackgroundColor(Color.GREEN);
        linha12.setBackgroundColor(Color.GREEN);

        // Linhas 8 e 11.
        linha8.setBackgroundColor(Color.MAGENTA);
        linha9.setBackgroundColor(Color.MAGENTA);
        linha10.setBackgroundColor(Color.MAGENTA);
        linha11.setBackgroundColor(Color.MAGENTA);

        // linha 13.
        linha13.setBackgroundColor(Color.BLUE);

        // linha 15.
        linha15.setBackgroundColor(Color.CYAN);
    }

    private void mostraExecucao(){
        // Colorir os nos.
        WelshPowell wp = new WelshPowell("color");
        wp.init(g);
        wp.compute();

        // Montar a matriz de adjacencia dos nos.
        montaMatriz();

        // Preparar as variaveis.
        int n = g.getNodeCount(), cromNum = wp.getChromaticNumber(), contK = 1, cont = 0;
        StringBuilder listCores = new StringBuilder();
        List<String> listNosAux1 = new ArrayList<>(listNos), listNosAux2 = new ArrayList<>(listNos);

        // Colorir o algoritmo.
        colorirAlgo();

        // Preenche a list cores Cn{}, vetores vazios.
        for (int i = 0; i < n; i++){
            listCores.append("C").append(i).append("{Ø}");
            if (i+1 < n)
                listCores.append(", ");
        }

        // Mostra a inicialização das variavesi.
        s.append("Inicialização: ").append(listCores).append(", Y = ").append(listNosAux1).append(";\n\n");

        // Começa a mostar a execução.
        for (int i = 0; i < n; i++){
            if (contK <= cromNum)
                s.append("K = ").append(contK).append("\n\n");
            for (int j = 0; j < n; j++){
                if (matrix[i][j] == 0){
                    String aux = listNosAux1.get(j);
                    s.append("i = ").append(aux).append(" , ");
                    s.append("N(").append(aux).append(") ∩ C").append(cont).append(" = Ø");
                    s.append(", então C").append(cont).append(" = ").append(aux).append(", Y - {").append(aux).append("}");
                    listNosAux2.remove(aux);
                    s.append(", Y = ").append(listNosAux2);
                    s.append("\n\n");
                    // Preenche a matriz com 2 para essa coluna nao ser mais vizitada.
                    for (int x = 0; x < n; x++){
                        matrix[x][j] = 2;
                    }
                } else if (matrix[i][j] == 1) {
                    String aux = listNosAux1.get(j);
                    s.append("i = ").append(aux).append(" , ");
                    s.append("N(").append(aux).append(") ∩ C").append(cont).append(" != Ø");
                    s.append("\n\n");
                }
            }
            contK++; cont++;
        }

        // Separa os nós coloridos e mostra ele nos vetores de cores.
        for (int i = 0; i < cromNum; i++){
            s.append("C").append(i).append("{");
            for (int j = 0; j < n; j++){
                int col = (int) g.getNode(j).getAttribute("color");
                if (col == i){
                    s.append(g.getNode(j).getId());
                    s.append(", ");
                }
            }
            s.append("}\n\n");
        }

        s.append("<-- <-- Se você voltar para a página '\'GRAFO'\' verá que seu grafo foi colorido.");

        // Retorna a string de execução.
        if (listener != null)
            listener.mandaTexto(chamou, String.valueOf(s));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HelperColoracao) {
            listener = (HelperColoracao) context;
        } else {
            throw new RuntimeException(context.toString() + getString(R.string.helper_coloracao));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface HelperColoracao{
        void mandaTexto(boolean chamou ,String alteraResp);
    }
}
