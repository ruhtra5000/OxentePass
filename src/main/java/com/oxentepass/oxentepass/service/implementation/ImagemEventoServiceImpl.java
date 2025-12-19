package com.oxentepass.oxentepass.service.implementation;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.oxentepass.oxentepass.entity.Evento;
import com.oxentepass.oxentepass.entity.Imagem;
import com.oxentepass.oxentepass.exceptions.ArquivoInvalidoException;
import com.oxentepass.oxentepass.exceptions.RecursoNaoEncontradoException;
import com.oxentepass.oxentepass.exceptions.TipoArquivoNaoSuportadoException;
import com.oxentepass.oxentepass.repository.EventoRepository;
import com.oxentepass.oxentepass.service.ImagemEventoService;

import io.github.cdimascio.dotenv.Dotenv;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

// Sub-service responsável por lidar com armazenamento de imagens de eventos
// utilizando o AWS S3

@Service
public class ImagemEventoServiceImpl implements ImagemEventoService {
    // Repositórios
    @Autowired
    private EventoRepository eventoRepository;

    //Cliente S3
    @Autowired
    private S3Client s3Client;

    // Outras variáveis
    Dotenv dotenv = Dotenv.load();
    private final String s3Bucket = dotenv.get("AWS_BUCKET_NAME");

    // Métodos auxiliares
    private Evento buscarEventoId(long id) {
        Optional<Evento> eventoBusca = eventoRepository.findById(id);

        if (eventoBusca.isEmpty())
            throw new RecursoNaoEncontradoException("O evento com id " + id + " não existe.");

        return eventoBusca.get();
    }

    private void verificarImagem(MultipartFile file) {
        // Verificação de conteúdo
        if(file.isEmpty())
            throw new ArquivoInvalidoException("Não há arquivo anexado.");

        // Verificação de tipo (suportados: jpeg, png e webp)
        String type = file.getContentType();
        if(!type.equals("image/jpeg") && !type.equals("image/png") && !type.equals("image/webp"))
            throw new TipoArquivoNaoSuportadoException("Tipo de arquivo não suportado. Tipos suportados: .jpeg, .png, .webp");

        // Verificação de tamanho (max: 5 MB)
        if(file.getSize() > 5L * 1024 * 1024)
            throw new ArquivoInvalidoException("A imagem excede o tamanho máximo de 5 MB por arquivo.");
    }

    private Imagem retornarImagemPorId(List<Imagem> imagens, long idImagem) {
        for (Imagem imagem : imagens) {
            if (imagem.getId() == idImagem)
                return imagem;
        }
        throw new RecursoNaoEncontradoException("A imagem com id " + idImagem + " não existe ou não está vinculada a este evento.");
    }

    // Comunicação com o AWS S3
    private void uploadImagemS3 (MultipartFile file, String chave) {
        try {
            PutObjectRequest request = PutObjectRequest.builder()
                .bucket(s3Bucket)
                .key(chave)
                .contentType(file.getContentType())
                .acl(ObjectCannedACL.PRIVATE)
                .build();
    
            s3Client.putObject(
                request, 
                RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );
        }
        catch (IOException err) {
            //Capturando e lançando uma exceção não-verificada
            //para ser capturada pelo controller advice

            //Nesse caso, eu quero que o cód. de erro seja 500,
            //então vou lançar essa exceção mais generica mesmo
            throw new RuntimeException("Erro ao processar o arquivo enviado.");
        }
    }

    private void deletarImagemS3(String chave) {
        DeleteObjectRequest request = DeleteObjectRequest.builder()
            .bucket(s3Bucket)
            .key(chave)
            .build();

        s3Client.deleteObject(request);
    }

    // Métodos da interface
    @Override
    @Transactional
    public void adicionarImagem(long idEvento, MultipartFile file, boolean ehCapa) {
        System.out.println("\n\n" + System.getenv("AWS_ACCESS_KEY_ID") + "\n\n");

        Evento evento = buscarEventoId(idEvento);
        
        verificarImagem(file);

        //Geração de chave
        String chave = "eventos/%d/imagens/%s.%s".formatted(
            idEvento,
            UUID.randomUUID(),
            file.getContentType()
        );

        uploadImagemS3(file, chave);

        Imagem imagem = new Imagem();
        imagem.setChaveS3(chave);
        imagem.setNome(evento.getNome() + "-" + file.getOriginalFilename());
        imagem.setTipoArquivo(file.getContentType());
        imagem.setEhCapa(ehCapa);

        evento.addImagem(imagem);
        eventoRepository.save(evento);
    }

    @Override
    public Page<Imagem> listarImagens(long idEvento, Pageable pageable) {
        return eventoRepository.findImagemByEventoId(idEvento, pageable);
    }

    @Override
    public void removerImagem(long idEvento, long idImagem) {
        Evento evento = buscarEventoId(idEvento);

        Imagem imagem = retornarImagemPorId(evento.getImagens(), idImagem);

        deletarImagemS3(imagem.getChaveS3());

        evento.removerImagem(imagem);
        eventoRepository.save(evento);
    }
}
