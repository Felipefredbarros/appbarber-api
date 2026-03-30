package com.appbarber.api.service;

import com.appbarber.api.model.Usuario;
import com.appbarber.api.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
    this.userRepository = repository;
    this.passwordEncoder = passwordEncoder;
    }

    public Usuario cadastrar(Usuario user){
        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new RuntimeException("Esse email ja esta cadastrado");
        }
        String senhaCript = passwordEncoder.encode(user.getSenha());
        user.setSenha(senhaCript);

        return userRepository.save(user);
    }
}
