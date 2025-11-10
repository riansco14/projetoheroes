package com.api.heroes.configs;

import com.api.heroes.model.Deck;
import com.api.heroes.model.Hero;
import com.api.heroes.model.Usuario;
import com.api.heroes.repository.DeckRepository;
import com.api.heroes.repository.HeroRepository;
import com.api.heroes.repository.UsuarioRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
public class DataSeeder {

    private final ObjectMapper mapper = new ObjectMapper();

    @Bean
    CommandLineRunner seedDatabase(HeroRepository heroRepo,
                                   UsuarioRepository usuarioRepo,
                                   DeckRepository deckRepo) {
        return args -> seed(heroRepo, usuarioRepo, deckRepo);
    }

    @Transactional
    void seed(HeroRepository heroRepo,
              UsuarioRepository usuarioRepo,
              DeckRepository deckRepo) throws Exception {

        // Torne o seed idempotente: só carrega se a base estiver vazia
        if (heroRepo.count() > 0 || usuarioRepo.count() > 0 || deckRepo.count() > 0) {
            return;
        }

        // 1) HEROES
        List<Hero> heroes = readJson("seed/heroes.json", new TypeReference<>() {});

        heroes.forEach(item->{
            item.setId(null);
            heroRepo.save(item);
        });



        // Mapear por nome para facilitar lookup
        Map<String, Hero> heroByName = heroRepo.findAll().stream()
                .collect(Collectors.toMap(Hero::getName, h -> h, (a,b)->a, LinkedHashMap::new));

        // 2) USUÁRIOS
        List<Usuario> usuarios = readJson("seed/usuarios.json", new TypeReference<>() {});
        final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


        // se usa senha criptografada, encode aqui (ex.: BCrypt)
        usuarios.forEach(item->{
            item.setSenha(encoder.encode(item.getSenha()));
            usuarioRepo.save(item);
        });

        Map<String, Usuario> usuarioByLogin = usuarioRepo.findAll().stream()
                .collect(Collectors.toMap(Usuario::getLogin, u -> u));
/*
        // 3) DECKS (usa logins e nomes de heróis para montar relações)
        List<DeckSeed> deckSeeds = readJson("seed/decks.json", new TypeReference<>() {});
        List<Deck> decks = new ArrayList<>();
        for (DeckSeed s : deckSeeds) {
            Usuario dono = Optional.ofNullable(usuarioByLogin.get(s.usuarioLogin()))
                    .orElseThrow(() -> new IllegalStateException("Usuario não encontrado: " + s.usuarioLogin()));

            List<Hero> hs = s.heroesNames() == null ? List.of() :
                    s.heroesNames().stream()
                            .map(n -> Optional.ofNullable(heroByName.get(n))
                                    .orElseThrow(() -> new IllegalStateException("Herói não encontrado: " + n)))
                            .toList();

            Deck d = new Deck();
            d.setNome(s.nome());
            d.setUsuario(dono);
            d.setHeroes(new ArrayList<>(hs));
            decks.add(d);
        }
        deckRepo.saveAll(decks);*/
    }

    private <T> T readJson(String classpathPath, TypeReference<T> type) throws Exception {
        ClassPathResource res = new ClassPathResource(classpathPath);
        try (InputStream is = res.getInputStream()) {
            byte[] bytes = is.readAllBytes();
            return mapper.readValue(new String(bytes, StandardCharsets.UTF_8), type);
        }
    }

    // DTO interno para o decks.json
    record DeckSeed(String nome, String usuarioLogin, List<String> heroesNames) {}
}