package com.teoriagrafos.athena;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.teoriagrafos.athena.interfaces.HelperGrafo;

import org.graphstream.algorithm.Dijkstra;
import org.graphstream.algorithm.coloring.WelshPowell;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.android_viewer.util.DefaultFragment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class GrafoOrientadoFragment extends Fragment implements DialogAddAresta.HelperAddAresta, DialogDeletar.HelperDeletar{

    // Variáveis para trabalhar na classe.
    private Graph graph;
    private String[] alfabeto = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "X", "W", "Y", "Z"};
    private int pos = 0, numCrom;
    private float x = 0.001f, y = 0.001f;
    StringBuilder s = new StringBuilder();
    private List<String> listNos = new ArrayList<>(), listArestas = new ArrayList<>(), listPeso = new ArrayList<>(), listNosColoridos;
    private HelperGrafo listener;

    // Variável com o estilo dos elementos do grafo.
    private String styleSheet = "graph { fill-color: #CDCDCD; }" +
            "node { size: 100px; text-size: 30px; fill-color: white; stroke-mode: plain; }" +
            "node:clicked { fill-color: #007FFF; }" +
            "edge { size: 5px; text-size: 40px; padding: 0, 25; fill-mode: plain; arrow-shape: arrow; arrow-size: 20px; }";

    public GrafoOrientadoFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_grafo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Pega o BottomNavigationView e seta um OnNavigationItemSelectedListener para fazer as ações.
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottomNavView);
        bottomNavigationView.setOnNavigationItemSelectedListener(selectedListener);

        graph = new MultiGraph("AthenaGraphOrientado");

        graph.setAttribute("ui.stylesheet", styleSheet);
        graph.setAttribute("ui.antialias");

        display(savedInstanceState, graph, false);
    }

    //  Método para o click de cada botão no BottomNavigationView.
    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener
            = menuItem -> {
        switch (menuItem.getItemId()) {
            case R.id.add_no:
                addNo();
                return true;

            case R.id.add_aresta:
                openDialogAddAresta();
                return true;

            case R.id.deletar:
                openDialogDeletar();
                return true;
        }
        return false;
    };

    // Método para criar um fragment e mostrar o grafos na tela.
    public void display(Bundle savedInstanceState, Graph graph, boolean autoLayout) {
        if (savedInstanceState == null) {
            FragmentManager fm = Objects.requireNonNull(getActivity()).getFragmentManager();
            DefaultFragment fragment = (DefaultFragment) fm.findFragmentByTag("fragment_tag");

            if (null == fragment) {
                fragment = new DefaultFragment();
                fragment.init(graph, autoLayout);
            }
            FragmentTransaction ft = fm.beginTransaction() ;
            ft.add(R.id.frameLayout, fragment).addToBackStack(null).commit();
        }
    }

    // Método para adicionar nó.
    public void addNo() {
        if (pos < alfabeto.length){
            String aux = alfabeto[pos];
            graph.addNode(aux);
            graph.getNode(aux).setAttribute("ui.label", aux);
            graph.getNode(aux).setAttribute("xyz", x, y, 0);
            listNos.add(aux);
            pos++;
            x += 0.005f;
            y += 0.005f;
        } else {
            chamaToast(getString(R.string.no_maximo));
        }
    }

    // Método para abir o DialogAddAresta.
    private void openDialogAddAresta() {
        DialogAddAresta dialogAddAresta = new DialogAddAresta();
        Bundle bundle = new Bundle();

        bundle.putStringArrayList("listNos", (ArrayList<String>) listNos);
        dialogAddAresta.setArguments(bundle);
        dialogAddAresta.setTargetFragment(this, 1);
        assert getActivity() != null;
        dialogAddAresta.show(getActivity().getSupportFragmentManager(), getString(R.string.add_aresta));
    }

    // Método para criar os nos com os dados do usuário.
    public void applyAddAresta(String noFrom, String noTo, String peso) {
        String nomeAresta = noFrom + noTo;
        int tam = graph.getEdgeCount();
        boolean arestaExiste = false;
        // Se tam == 0 não tem aresta então cria uma direto.
        if (tam == 0){
            if (peso.equals("")){
                peso = "1";
                graph.addEdge(nomeAresta, noFrom, noTo, true)
                        .setAttribute("length", peso);
                listArestas.add(nomeAresta);
                listPeso.add(peso);
            } else {
                graph.addEdge(nomeAresta, noFrom, noTo, true)
                        .setAttribute("length", peso);
                graph.getEdge(nomeAresta).setAttribute("ui.label", peso);
                listArestas.add(nomeAresta);
                listPeso.add(peso);
            }
        }
        // Se tam != 0 então já tem aresta, tem que olhar uma a uma antes de add uma nova.
        if (tam != 0){
            for (int i = 0; i < tam; i++) {
                String compara = graph.getEdge(i).getId();
                if (compara.equals(nomeAresta)) {
                    arestaExiste = true;
                }
            }
        }
        // Se arestaExiste == true a aresta já existe caso contrario cria a aresta.
        if (arestaExiste){
            chamaToast(getString(R.string.aresta_existente));
        } else if (tam >= 1){
            if (peso.equals("")){
                peso = "1";
                graph.addEdge(nomeAresta, noFrom, noTo, true)
                        .setAttribute("length", peso);
                listArestas.add(nomeAresta);
                listPeso.add(peso);
            } else {
                graph.addEdge(nomeAresta, noFrom, noTo, true)
                        .setAttribute("length", peso);
                graph.getEdge(nomeAresta).setAttribute("ui.label", peso);
                listArestas.add(nomeAresta);
                listPeso.add(peso);
            }
        }
    }

    // Método para abir o DialogDeletar.
    private void openDialogDeletar(){
        DialogDeletar dialogDeletar = new DialogDeletar();
        Bundle bundle = new Bundle();

        bundle.putStringArrayList("listNos", (ArrayList<String>) listNos);
        bundle.putStringArrayList("listArestas", (ArrayList<String>) listArestas);
        dialogDeletar.setArguments(bundle);
        dialogDeletar.setTargetFragment(this, 1);
        assert getActivity() != null;
        dialogDeletar.show(getActivity().getSupportFragmentManager(), getString(R.string.deletar));
    }

    // Método para deletar os nos e arestas com os dados do usuário.
    @Override
    public void applyDeletar(String deletaNo, String deletaAresta, int locPeso, boolean controle) {
        // Se controle == true deleta o nó senão deleta aresta.
        if (controle){
            graph.removeNode(deletaNo);
            listNos.remove(deletaNo);
            atualizaLista();
        } else {
            graph.removeEdge(deletaAresta);
            listArestas.remove(deletaAresta);
            listPeso.remove(locPeso);
        }
    }

    // Método para atualizar a lista de arestas ao deltar um nó.
    private void atualizaLista(){
        int tam = graph.getEdgeCount();
        listArestas = new ArrayList<>();
        for (int i = 0; i < tam; i++)
            listArestas.add(graph.getEdge(i).getId());
    }

    // Método que chama um Toast para facilitar o uso do mesmo.
    private void chamaToast(String mensagem){
        Toast.makeText(getContext(), mensagem, Toast.LENGTH_SHORT).show();
    }

    public void validaEnviar(){
        if (listener != null)
            listener.enviarInformacoes(true, listNos, listArestas, listPeso);
    }

    // Método para colorir os nós do grafo.
    public void executarColoracao(){
        WelshPowell wp = new WelshPowell("color");
        wp.init(graph);
        wp.compute();

        numCrom = wp.getChromaticNumber();

        String[] colors = new String[numCrom];
        listNosColoridos = new ArrayList<>();

        // Preenche a vetor de cores com cores aleatorias.
        for (int i = 0; i < numCrom; i++){
            colors[i] = "#" + numeroHexAleatorio() + numeroHexAleatorio() + numeroHexAleatorio();
        }

        // Pega os nós e pinta eles usando as cores aleatorias.
        for (Node n: graph){
            int col = (int) n.getNumber("color");
            n.setAttribute("ui.style", "fill-color: "+colors[col]+";");
            listNosColoridos.add(n.getId()+col);
        }
    }

    // Método que retorna um numero hexdecimal aleatorio.
    private String numeroHexAleatorio(){
        String[] cores = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        Random pos = new Random();
        int i = pos.nextInt(15);
        return cores[i];
    }

    // Método para colorir o caminho minimo.
    public void executarDijkstra(String noOrigem, String noDestino){
        Dijkstra d = new Dijkstra(Dijkstra.Element.EDGE, "result", "length");
        d.init(graph);
        d.setSource(graph.getNode(noOrigem));
        d.compute();

        // Colorir em verde todos os nós do caminho minimo.
        for (Node node : d.getPathNodes(graph.getNode(noDestino)))
            node.setAttribute("ui.style", "fill-color: green;");

        // Colorir em vermelho todos as arestas do caminho minimo.
        for (Edge edge : d.getTreeEdges())
            edge.setAttribute("ui.style", "fill-color: red;");
    }

    public  void executaProfundidade(String noOrigem){
        Node n = graph.getNode(noOrigem);
        Iterator<? extends  Node> k = n.getDepthFirstIterator();

        while (k.hasNext()){
            Node next = k.next();
            next.setAttribute("ui.style", "fill-color: green;");
        }
    }

    public  void executaLargura(String noOrigem){
        Node n = graph.getNode(noOrigem);
        Iterator<? extends  Node> k = n.getBreadthFirstIterator();

        while (k.hasNext()){
            Node next = k.next();
            next.setAttribute("ui.style", "fill-color: green;");
        }
    }

    // Limpa a coloração do grafo.
    public void limparCorGrafo(){
        if (!listNos.isEmpty()) {
            for (Node n : graph) {
                n.setAttribute("ui.style", "fill-color: white;");
            }

            for (int i = 0; i < listArestas.size(); i++) {
                graph.getEdge(listArestas.get(i)).setAttribute("ui.style", "fill-color: black;");
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HelperGrafo) {
            listener = (HelperGrafo) context;
        } else {
            throw new RuntimeException(context.toString() + getString(R.string.helper_grafo));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}