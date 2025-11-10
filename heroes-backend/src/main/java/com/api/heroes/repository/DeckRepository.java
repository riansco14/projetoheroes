package com.api.heroes.repository;

import com.api.heroes.model.Deck;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeckRepository extends JpaRepository<Deck, Long> {
    List<Deck> findByUsuarioId(Long usuarioId);
}
