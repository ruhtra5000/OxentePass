package com.oxentepass.oxentepass.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.oxentepass.oxentepass.entity.Imagem;

public interface ImagemEventoService {
    public void adicionarImagem(long idEvento, MultipartFile file, boolean ehCapa);
    public Page<Imagem> listarImagens(long idEvento, Pageable pageable);
    public void removerImagem(long idEvento, long idImagem);
}
