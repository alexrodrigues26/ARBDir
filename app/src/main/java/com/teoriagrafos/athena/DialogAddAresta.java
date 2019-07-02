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
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class DialogAddAresta extends AppCompatDialogFragment {

    // Variáveis para controlar as acoes (obs.) ViewGroup só foi criado para não passar null no inflate.
    private ViewGroup viewGroup;
    private Spinner spFrom, spTo;
    private EditText edtPeso;
    private List<String> listNos;
   private HelperAddAresta listner;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Infla o layout para o dialog.
        assert getActivity() != null;
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_dialog_add_aresta, viewGroup, false);

        assert getArguments() != null;
        // Pega a lista de nós que veio via Bundle.
        listNos = getArguments().getStringArrayList("listNos");

        // Pega os campos para capturas os dados.
        spFrom = view.findViewById(R.id.sp_from);
        spTo = view.findViewById(R.id.sp_to);
        edtPeso = view.findViewById(R.id.edt_peso);

        // Cria um ArrayAdapter para preencher o spinner.
        ArrayAdapter<String> nos = new  ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listNos);
        nos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFrom.setAdapter(nos);
        spTo.setAdapter(nos);

        // Impede a janela de ser fechada sem que o botão cancelar seja clicado.
        setCancelable(false);

        // Programa as acoes dos botões positivo e negativo.
        builder.setView(view)
                .setTitle(getString(R.string.add_aresta))
                .setNegativeButton(getString(R.string.btn_cancelar),
                        (dialog, which) -> dismiss())
                .setPositiveButton(getString(R.string.btn_add),
                        (dialog, which) -> {
                            String noFrom = spFrom.getSelectedItem().toString();
                            String noTo = spTo.getSelectedItem().toString();
                            String peso = edtPeso.getText().toString();
                            if (listner != null)
                                listner.applyAddAresta(noFrom, noTo, peso);
                        });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listner = (HelperAddAresta) getTargetFragment();
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + getString(R.string.helper_add));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listner = null;
    }

    public interface HelperAddAresta{
        void applyAddAresta(String noFrom, String noTo, String peso);
    }
}
