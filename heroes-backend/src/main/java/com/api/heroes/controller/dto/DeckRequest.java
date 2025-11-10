package com.api.heroes.controller.dto;

import java.util.List;
import java.util.Optional;

public record DeckRequest(
        Optional<String> nomeDeck,
        List<Long> heroesIds
) {}