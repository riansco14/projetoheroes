package com.api.heroes.service;

import com.api.heroes.controller.dto.UsuarioLoginDTO;
import com.api.heroes.controller.dto.UsuarioRegistroDTO;
import com.api.heroes.model.Usuario;
import com.api.heroes.repository.UsuarioRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UsuarioRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UsuarioRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario signup(UsuarioRegistroDTO input) {
        Usuario user = new Usuario();

        user.setNome(input.getNome());
        user.setLogin(input.getLogin());
        user.setSenha(passwordEncoder.encode(input.getSenha()));

        return userRepository.save(user);
    }

    public Usuario authenticate(UsuarioLoginDTO input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getLogin(),
                        input.getSenha()
                )
        );

        return userRepository.findByLogin(input.getLogin())
                .orElseThrow();
    }
}