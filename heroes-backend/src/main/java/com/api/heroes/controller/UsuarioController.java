package com.api.heroes.controller;

import com.api.heroes.model.Deck;
import com.api.heroes.model.Usuario;
import com.api.heroes.service.UsuarioService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.api.heroes.controller.dto.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @PostMapping
    public Usuario criarUsuario(@RequestBody Usuario usuario) {
        return service.criarUsuario(usuario);
    }


    @GetMapping("/me")
    public UsuarioWithDeckResponse authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Usuario currentUser = (Usuario) authentication.getPrincipal();
        return service.getUserByLogin(currentUser.getLogin());
    }

    @PostMapping("/deck")
    public DeckResponse salvarDeck(@RequestBody DeckRequest deckRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Usuario currentUser = (Usuario) authentication.getPrincipal();
        return service.criarDeckParaUsuario(currentUser.getId(), "Deck", deckRequest.heroesIds());
    }

    @PutMapping("/deck/{idDeck}")
    public DeckResponse atualizarDeck(@PathVariable Long idDeck, @RequestBody DeckRequest deckRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Usuario currentUser = (Usuario) authentication.getPrincipal();
        return service.editarDeckParaUsuario(idDeck, currentUser.getId(), deckRequest.nomeDeck().orElse("Deck"), deckRequest.heroesIds());
    }

}