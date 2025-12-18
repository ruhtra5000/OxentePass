package com.oxentepass.oxentepass.service.implementation;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.oxentepass.oxentepass.entity.Usuario;
import com.oxentepass.oxentepass.exceptions.EstadoInvalidoException;
import com.oxentepass.oxentepass.exceptions.RecursoDuplicadoException;
import com.oxentepass.oxentepass.exceptions.RecursoNaoEncontradoException;
import com.oxentepass.oxentepass.repository.UsuarioRepository;
import com.oxentepass.oxentepass.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void cadastrarUsuario(Usuario usuario) {

        String cpfLimpo = usuario.getCpf().replaceAll("\\D", "");
        usuario.setCpf(cpfLimpo);

        Optional<Usuario> existingUser = repository.findByCpfOrEmail(cpfLimpo, usuario.getEmail());

        if (existingUser.isPresent()) {
            throw new RecursoDuplicadoException("Já existe um usuário registrado com este CPF ou Email");
        }

        String senhaCriptografada = passwordEncoder.encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);

        repository.save(usuario);
    };

    @Override
    public void loginUsuario(String cpf, String senha) {
        String cpfLimpo = cpf.replaceAll("\\D", "");
        
        Optional<Usuario> optionalUser = repository.findByCpf(cpfLimpo);

        if (optionalUser.isEmpty()) {
            throw new RecursoNaoEncontradoException("Nenhum usuário encontrado para o cpf " + cpf);
        }

        Usuario usuario = optionalUser.get();

        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            throw new EstadoInvalidoException("Senha incorreta para este usuário");
        }
    };

    @Override
    public void editarUsuario(long id, Usuario dados) {

        Optional<Usuario> optionalUser = repository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new RecursoNaoEncontradoException("Usuário com id " + id + " não encontrado!");
        }

        Usuario user = optionalUser.get();

        if (!dados.getEmail().equals(user.getEmail()) && repository.findByEmail(dados.getEmail()).isPresent()) {
            throw new RecursoDuplicadoException("O e-mail " + dados.getEmail() + " já está registrado no sistema!");
        }   

        user.setEmail(dados.getEmail());
        user.setNome(dados.getNome());

        repository.save(user);
    };

    @Override
    public void deletarUsuario(long id) {

        Optional<Usuario> user = repository.findById(id);

        if (user.isEmpty()) {
            throw new RecursoNaoEncontradoException("O usuário com id " + id + " não foi encontrado!");
        }

        repository.delete(user.get());
    }

    @Override
    public Page<Usuario> listarUsuarios(Pageable pageable) {
        return repository.findAll(pageable);
    }

}