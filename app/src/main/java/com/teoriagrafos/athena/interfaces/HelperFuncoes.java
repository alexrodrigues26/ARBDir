package com.teoriagrafos.athena.interfaces;

public interface HelperFuncoes{
    void pegarInformacoes(boolean chamou, String funcao);
    void pintarCaminhoMinimo(boolean chamou, String noOrigem, String noDestino);
    void executarBusca(boolean chamou, String algo, String noOrigem);
    void limpar(boolean chamou);
}
