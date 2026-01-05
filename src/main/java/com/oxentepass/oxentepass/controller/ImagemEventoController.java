package com.oxentepass.oxentepass.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.oxentepass.oxentepass.entity.Imagem;
import com.oxentepass.oxentepass.service.ImagemEventoService;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Arthur de Sá
 * Controller para lidar com o upload de imagens de eventos, através de ImagemEventoService
 */

@RestController
@RequestMapping("/evento/{idEvento}/imagens")
public class ImagemEventoController {
    
    @Autowired
    private ImagemEventoService imagemEventoService;

    @Operation(summary = "Adicionar Imagem a Evento", description = "Cria uma nova Imagem (seja capa ou não) para o Evento com id especificado")
    @PostMapping(value = "/capa/{ehCapa}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> criarImagem (
        @PathVariable long idEvento, 
        @PathVariable boolean ehCapa, 
        @RequestParam("file") MultipartFile file)
    {
        imagemEventoService.adicionarImagem(idEvento, file, ehCapa);
        
        return new ResponseEntity<String>(
            "Imagem " + file.getOriginalFilename() + " adicionada ao evento com id " + idEvento + " com sucesso!",
            HttpStatus.CREATED
        );
    }

    @Operation(summary = "Listar Imagens de um Evento", description = "Retorna os metadados das Imagens vinculadas a um Evento com id especificado")
    @GetMapping
    public ResponseEntity<Page<Imagem>> listarImagens (@PathVariable long idEvento, Pageable pageble) {
        return new ResponseEntity<Page<Imagem>>(
            imagemEventoService.listarImagens(idEvento, pageble),
            HttpStatus.OK
        );
    }
    
    @Operation(summary = "Deletar Imagem de um Evento", description = "Exclui uma Imagem com id especificado para o Evento com id especificado")
    @DeleteMapping("/{idImagem}")
    public ResponseEntity<String> removerImagem (@PathVariable long idEvento, @PathVariable long idImagem) {
        imagemEventoService.removerImagem(idEvento, idImagem);

        return new ResponseEntity<String>(
            "Imagem com id " + idImagem + " removida do evento com id " + idEvento + " com sucesso!",
            HttpStatus.OK
        );
    }
}
