package com.teoriagrafos.athena.interfaces;

import java.util.List;

public interface HelperGrafo {
    void enviarInformacoes(boolean chamou, List<String> listNos, List<String> listArestas, List<String> listPeso);
    void enviaColoracaoResp(boolean chamou, String resp);
}