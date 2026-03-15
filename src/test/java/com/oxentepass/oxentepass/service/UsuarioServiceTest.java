package com.oxentepass.oxentepass.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.oxentepass.oxentepass.entity.Usuario;
import com.oxentepass.oxentepass.exceptions.EstadoInvalidoException;
import com.oxentepass.oxentepass.exceptions.RecursoDuplicadoException;
import com.oxentepass.oxentepass.exceptions.RecursoNaoEncontradoException;
import com.oxentepass.oxentepass.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

/**
 * @author Guilherme Paes
 * Testes de integração para UsuarioService
 */

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UsuarioServiceTest {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private UsuarioService service;

    private Usuario userBase;

    // Configuração inicial antes de cada teste
    @BeforeEach
    void setup() {
        userBase = new Usuario();
        userBase.setNome("Guy Fawkes");
        userBase.setCpf("176.176.176-17");
        userBase.setEmail("guy.fawkes@email.com");
        userBase.setSenha("vendetta");
    }

    // Teste para registrar um novo usuário com sucesso
    @Test
    public void deveRegistrarUsuarioTest() {
        long qtdeInicial = repository.count();

        service.cadastrarUsuario(userBase);

        assertThat(repository.count()).isEqualTo(qtdeInicial + 1);

        Usuario salvo = repository.findByCpf("17617617617").orElseThrow();
        assertThat(salvo.getNome()).isEqualTo("Guy Fawkes");
    }

    // Teste para rejeitar registro de usuário com CPF já existente
    @Test
    public void deveRejeitarRegistroDuplicadoUsuarioTest() {
        service.cadastrarUsuario(userBase);

        // Tentativa de duplicar o mesmo CPF
        assertThrows(RecursoDuplicadoException.class, () -> service.cadastrarUsuario(userBase));
    }

    // Teste para autenticar usuário com sucesso
    @Test
    public void deveAutenticarUsuarioTest() {
        service.cadastrarUsuario(userBase);
        assertDoesNotThrow(() -> service.loginUsuario("17617617617", "vendetta"));
    }

    // Teste para rejeitar autenticação com senha incorreta
    @Test
    public void deveRejeitarSenhaUsuarioTest() {
        service.cadastrarUsuario(userBase);
        assertThrows(EstadoInvalidoException.class,
                () -> service.loginUsuario("17617617617", "senhaErrada"));
    }

    // Teste para atualizar dados do usuário com sucesso
    @Test
    public void deveAtualizarUsuarioTest() {
        service.cadastrarUsuario(userBase);

        userBase.setNome("Guido Fawkes");
        userBase.setEmail("guido@email.com");
        service.editarUsuario(userBase.getId(), userBase);

        Usuario atualizado = repository.findById(userBase.getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado para este ID"));
        assertThat(atualizado.getNome()).isEqualTo("Guido Fawkes");
        assertThat(atualizado.getEmail()).isEqualTo("guido@email.com");
    }
}