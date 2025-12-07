package com.oxentepass.oxentepass.service;

import com.oxentepass.oxentepass.entity.Ingresso;

public interface IngressoService {
    // Operações Básicas
    public void cadastrarIngresso(Ingresso ingresso);
    public void deletarIngresso(Long id);
    public Iterable<Ingresso> listarTodosIngressos();
    public Ingresso buscarIngressoPorId(Long id);
    public Ingresso buscarIngressPorTipo(String tipo);
    public Ingresso ingressosDisponiveis(Long idEvento);
}