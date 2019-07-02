package com.teoriagrafos.athena;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class DialogSelecionaNos extends AppCompatDialogFragment {

    // Variáveis para controlar as acoes (obs.) ViewGroup só foi criado para não passar null no inflate.
    private ViewGroup viewGroup;
    private Spinner spNoOrigem, spNoDestino;
    private List<String> listNos = new ArrayList<>();
    private HelperSelecionaNo listner;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Infla o layout para o dialog.
        assert getActivity() != null;
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_dialog_seleciona_nos, viewGroup, false);

        assert getArguments() != null;
        // Pega os dados que chegaram via Bundle.
        listNos = getArguments().getStringArrayList("listNos");

        // Pega os campos para capturas os dados.
        spNoOrigem = view.findViewById(R.id.spNoOrigem);
        spNoDestino = view.findViewById(R.id.spNoDestino);

        // Cria um ArrayAdapter para nós e preencher os spinner.
        ArrayAdapter<String> nos = new  ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listNos);
        nos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spNoOrigem.setAdapter(nos);
        spNoDestino.setAdapter(nos);

        // Impede a janela de ser fechada sem que o botão cancelar seja clicado.
        setCancelable(false);

        // Programa as acoes dos botões positivo e negativo.
        builder.setView(view)
                .setTitle(getString(R.string.caminho_minimo))
                .setNegativeButton(getString(R.string.btn_cancelar),
                        (dialog, which) -> dismiss())
                .setPositiveButton(getString(R.string.executar),
                        (dialog, which) -> {
                            String origem = spNoOrigem.getSelectedItem().toString();
                            String destino = spNoDestino.getSelectedItem().toString();
                            if (listner != null) {
                                listner.applySelecionaNos(origem, destino);
                            }
                        });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listner = (HelperSelecionaNo) getTargetFragment();
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + getString(R.string.helper_seleciona_no));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listner = null;
    }

    public interface HelperSelecionaNo{
        void applySelecionaNos(String noOrigem, String noDestino);
    }
}
