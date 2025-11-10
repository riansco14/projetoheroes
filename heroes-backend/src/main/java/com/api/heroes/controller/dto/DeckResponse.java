package com.api.heroes.controller.dto;

import com.api.heroes.model.Hero;

import java.util.List;


public record DeckResponse(
        Long id,
        String nome,
        List<Hero> heroes
) {}