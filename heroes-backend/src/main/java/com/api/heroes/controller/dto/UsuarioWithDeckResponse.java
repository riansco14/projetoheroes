package com.api.heroes.controller.dto;


import java.util.List;

public record UsuarioWithDeckResponse(
        String nome,
        String login,
        List<DeckResponse> decks
) {}