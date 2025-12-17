package com.oxentepass.oxentepass.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

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

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UsuarioServiceTest {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private UsuarioService service;

    @Test
    public void deveRegistrarUsuarioTest() {

        List<Usuario> usuariosAnteriores = repository.findAll();

        Usuario user = new Usuario();
        user.setNome("Guy Fawkes");
        user.setCpf("17617617617");
        user.setEmail("guy.fawkes@email.com");
        user.setSenha("vendetta");

        repository.save(user);

        List<Usuario> usuariosPosteriores = repository.findAll();

        String usuarioCPF = usuariosPosteriores.get(0).getCpf();

        assertEquals(usuarioCPF, "17617617617");
        assertEquals(usuariosAnteriores.size() + 1, usuariosPosteriores.size());

    }

    @Test
    public void deveRejeitarRegistroUsuarioTest() {
        Usuario user = new Usuario();
        user.setNome("Guy Fawkes");
        user.setCpf("17617617617");
        user.setEmail("guy.fawkes@email.com");
        user.setSenha("vendetta");

        repository.save(user);

        assertThrows(RecursoDuplicadoException.class, () -> service.cadastrarUsuario(user));
    }

    @Test
    public void deveAutenticarUsuarioTest() {

        Usuario user = new Usuario();
        user.setNome("Guy Fawkes");
        user.setCpf("17617617617");
        user.setEmail("guy.fawkes@email.com");
        user.setSenha("vendetta");

        repository.save(user);

        assertDoesNotThrow(() -> service.loginUsuario("17617617617", "vendetta"));
    }

    @Test
    public void deveRejeitarCpfUsuarioTest() {

        Usuario user = new Usuario();
        user.setNome("Guy Fawkes");
        user.setCpf("17617617617");
        user.setEmail("guy.fawkes@email.com");
        user.setSenha("vendetta");

        repository.save(user);

        assertThrows(RecursoNaoEncontradoException.class,
                () -> service.loginUsuario("17617617617", "vendetta"));
    }

    @Test
    public void deveRejeitarSenhaUsuarioTest() {

        Usuario user = new Usuario();

        user.setNome("Guy Fawkes");
        user.setCpf("17617617617");
        user.setEmail("guy.fawkes@email.com");
        user.setSenha("vendetta");

        repository.save(user);

        assertThrows(EstadoInvalidoException.class,
                () -> service.loginUsuario("17617617617", "vendeta"));
    }

    @Test
    public void deveAtualizarUsuarioTest() {

        Usuario user = new Usuario();

        user.setNome("Guy Fawkes");
        user.setCpf("17617617617");
        user.setEmail("guy.fawkes@email.com");
        user.setSenha("vendetta");

        repository.save(user);

        List<Usuario> usuariosAnteriores = repository.findAll();
        String nomeUsuarioDesatualizado = usuariosAnteriores.get(0).getNome();

        user.setNome("Guido Fawkes");
        user.setCpf("17171717171");
        user.setEmail("guido.fawkes@email.com");
        user.setSenha("VforVendetta");

        service.editarUsuario(1L, user);

        List<Usuario> usuariosPosteriores = repository.findAll();
        String nomeUsuarioAtualizado = usuariosPosteriores.get(0).getNome();

        assertEquals(usuariosAnteriores, usuariosPosteriores);
        assertEquals(nomeUsuarioDesatualizado, "Guy Fawkes");
        assertEquals(nomeUsuarioAtualizado, "Guido Fawkes");

    }

}
