package com.api.heroes.service;


import com.api.heroes.controller.dto.DeckResponse;
import com.api.heroes.controller.dto.UsuarioWithDeckResponse;
import com.api.heroes.model.Deck;
import com.api.heroes.model.Hero;
import com.api.heroes.model.Usuario;
import com.api.heroes.repository.DeckRepository;
import com.api.heroes.repository.HeroRepository;
import com.api.heroes.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final HeroRepository heroRepository;
    private final DeckRepository deckRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


    public Usuario criarUsuario(Usuario usuario) {
        usuario.setSenha(encoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    public UsuarioWithDeckResponse getUserByLogin(String login) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByLogin(login);

        if (usuarioOptional.isPresent()) {
            Usuario usuarioDb = usuarioOptional.get();

            return new UsuarioWithDeckResponse(usuarioDb.getNome(), usuarioDb.getLogin(), usuarioDb.getDecks().stream()
                    .map(h -> new DeckResponse(h.getId(), h.getNome(), h.getHeroes()))
                    .toList());
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Esse herói não se encontra em nossos bancos de dados");
    }

    public DeckResponse criarDeckParaUsuario(Long usuarioId, String nomeDeck, List<Long> idsHeroes) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        List<Hero> heroes = heroRepository.findAllById(idsHeroes);

        Deck deck = new Deck();
        deck.setNome(nomeDeck);
        deck.setUsuario(usuario);
        deck.setHeroes(heroes);

        Deck deckSaved = deckRepository.save(deck);
        return new DeckResponse(deckSaved.getId(), deckSaved.getNome(), deckSaved.getHeroes());
    }

    public DeckResponse editarDeckParaUsuario(Long idDeck, Long usuarioId, String nomeDeck, List<Long> idsHeroes) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Deck deckExistente = deckRepository.findById(idDeck)
                .orElseThrow(() -> new RuntimeException("Deck não encontrado"));

        List<Hero> heroes = heroRepository.findAllById(idsHeroes);


        deckExistente.setNome(nomeDeck);
        deckExistente.setHeroes(heroes);

        Deck deckSaved = deckRepository.save(deckExistente);
        return new DeckResponse(deckSaved.getId(), deckSaved.getNome(), deckSaved.getHeroes());
    }
}
