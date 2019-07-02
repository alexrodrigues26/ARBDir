package com.teoriagrafos.athena;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class DialogDeletar extends AppCompatDialogFragment {

    // Variáveis para controlar as acoes (obs.) ViewGroup só foi criado para não passar null no inflate.
    private ViewGroup viewGroup;
    private Spinner spinner;
    private RadioButton rbNos, rbArestas;
    private boolean chave = true;
    private List<String> listNos = new ArrayList<>(), listArestas = new ArrayList<>();
    private HelperDeletar listner;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Infla o layout para o dialog.
        assert getActivity() != null;
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_dialog_deletar, viewGroup, false);

        assert getArguments() != null;
        // Pega os dados que chegaram via Bundle.
        listNos = getArguments().getStringArrayList("listNos");
        listArestas = getArguments().getStringArrayList("listArestas");

        // Pega os campos para capturas os dados.
        spinner = view.findViewById(R.id.spDeletar);
        rbNos = view.findViewById(R.id.rbNos);
        rbArestas = view.findViewById(R.id.rbArestas);

        // Cria dois ArrayAdapter um para nós e outro para arestas e preencher os spinner.
        ArrayAdapter<String> nos = new  ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listNos);
        nos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> arestas = new  ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listArestas);
        arestas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(nos);

        // Muda o conteúdo do spinner de acordo com o rádio button selecionado.
        rbNos.setOnClickListener(v -> {
            spinner.setAdapter(nos);
            chave = true;
        });

        rbArestas.setOnClickListener(v -> {
            spinner.setAdapter(arestas);
            chave = false;
        });

        // Impede a janela de ser fechada sem que o botão cancelar seja clicado.
        setCancelable(false);

        // Programa as acoes dos botões positivo e negativo.
        builder.setView(view)
                .setTitle(getString(R.string.deletar))
                .setNegativeButton(getString(R.string.btn_cancelar),
                        (dialog, which) -> dismiss())
                .setPositiveButton(getString(R.string.deletar),
                        (dialog, which) -> {
                            String itemSelecionado = spinner.getSelectedItem().toString();
                            int locPeso = spinner.getSelectedItemPosition();
                            String deletaNo;
                            String deletaAresta;
                            boolean controle;
                            if (listner != null) {
                                if (chave) {
                                    // Se chave == true deleta nó.
                                    listner.applyDeletar(itemSelecionado, itemSelecionado, locPeso, true);
                                } else {
                                    // Se chave == false deleta aresta.
                                    listner.applyDeletar(itemSelecionado, itemSelecionado, locPeso, false);
                                }
                            }
                        });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listner = (HelperDeletar) getTargetFragment();
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + getString(R.string.helper_deletar));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listner = null;
    }

    public interface HelperDeletar{
        void applyDeletar(String deletaNo, String deletaAresta, int locPeso, boolean controle);
    }
}
