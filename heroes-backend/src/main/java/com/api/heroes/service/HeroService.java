package com.api.heroes.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.api.heroes.client.HeroApiClient;
import com.api.heroes.controller.dto.*;
import com.api.heroes.model.Hero;
import com.api.heroes.repository.HeroRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class HeroService {

    private final HeroRepository heroRepository;

    public List<HeroResponse> getHeroes(String heroName) {

        List<Hero> heroesDb;
        List<HeroResponse> response = new ArrayList<>();
        //List<Hero> heroesDb = heroRepository.findAll();

        if(heroName == null) {
            heroesDb = heroRepository.findAll();
        } else {
            heroesDb = heroRepository.findAllByName(heroName);
        }

        for (Hero hero : heroesDb) {
            response.add(new HeroResponse(hero.getId(), hero.getName(),
                    hero.getAlterEgo(),
                    hero.getRace(),
                    hero.getGender(),
                    hero.getPlaceOfBirth(),
                    hero.getImageUrl(),
                    hero.getIntelligence(),
                    hero.getStrength(),
                    hero.getSpeed(),
                    hero.getPower()));
        }

        return response;
    }

    @Cacheable("heroes")
    public HeroResponsePaginationDto getHeroesWithPagination(String heroName, Integer page, Integer sizePage) {

        Pageable pageable = PageRequest.of(page,
                sizePage,
                Sort.by("id").ascending());

        //fazer a consulta paginada
        Page<Hero> heroesPaginated = heroRepository.findByNameWithPagination(heroName, pageable);
        //mapear o conteudo da pagina de Banco para BancoResponse
        List<Hero> heroesDb = heroesPaginated.getContent();
        List<HeroResponse> heroesResponse = heroesDb.stream()
                .map(hero -> new HeroResponse( hero.getId(),
                        hero.getName(),
                        hero.getAlterEgo(),
                        hero.getRace(),
                        hero.getGender(),
                        hero.getPlaceOfBirth(),
                        hero.getImageUrl(),
                        hero.getIntelligence(),
                        hero.getStrength(),
                        hero.getSpeed(),
                        hero.getPower()))
                .toList();

        //construir o objeto de resposta com as infos de paginação e o conteudo
        return new HeroResponsePaginationDto(heroesPaginated.getTotalPages(),
                heroesPaginated.getTotalElements(),
                heroesPaginated.getSize(),
                heroesPaginated.getNumber(),
                heroesResponse);
    }

    @CacheEvict(value="heroes", allEntries = true)
    public HeroResponse addHero(HeroRequest request) {
        try {
            Hero newHero = new Hero(null,
                    request.getName(),
                    request.getAlterEgo(),
                    request.getRace(),
                    request.getGender(),
                    request.getPlaceOfBirth(),
                    request.getImageUrl(),
                    request.getIntelligence(),
                    request.getStrength(),
                    request.getSpeed(),
                    request.getPower()
            );

            List<Hero> existingHero = heroRepository.findAnyEqualHero(
                    request.getName(),
                    request.getAlterEgo(),
                    request.getGender(),
                    request.getPlaceOfBirth()
            );

            if(!existingHero.isEmpty()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Já existe um hero com essas caracteristicas");
            }

            Hero heroSalvo = heroRepository.save(newHero);

            return new HeroResponse(heroSalvo.getId(), heroSalvo.getName(),
                    heroSalvo.getAlterEgo(),
                    heroSalvo.getRace(),
                    heroSalvo.getGender(),
                    heroSalvo.getPlaceOfBirth(),
                    heroSalvo.getImageUrl(),
                    heroSalvo.getIntelligence(),
                    heroSalvo.getStrength(),
                    heroSalvo.getSpeed(),
                    heroSalvo.getPower());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Já existe um herói com essas caracteristicas");
        }
    }

    @Cacheable("heroes")
    public HeroResponse getHeroByCode(Long id) {
        Optional<Hero> heroOptional = heroRepository.findById(id);

        if (heroOptional.isPresent()) {
            Hero heroDb = heroOptional.get();
            return new HeroResponse(heroDb.getId(), heroDb.getName(),
                    heroDb.getAlterEgo(),
                    heroDb.getRace(),
                    heroDb.getGender(),
                    heroDb.getPlaceOfBirth(),
                    heroDb.getImageUrl(),
                    heroDb.getIntelligence(),
                    heroDb.getStrength(),
                    heroDb.getSpeed(),
                    heroDb.getPower());
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Esse herói não se encontra em nossos bancos de dados");
    }

    @CacheEvict(value="heroes", allEntries = true)
    public HeroResponse refreshDb(Long id, HeroRequest request) {
        Optional<Hero> heroesDbOptional = heroRepository.findById(id);

        if (heroesDbOptional.isPresent()) {
            Hero heroDb = heroesDbOptional.get();

            heroDb.setName(request.getName());
            heroDb.setAlterEgo(request.getAlterEgo());
            heroDb.setGender(request.getGender());
            heroDb.setPlaceOfBirth(request.getPlaceOfBirth());

            List<Hero> existingHero = heroRepository.findAnyEqualHero(
                    request.getName(),
                    request.getAlterEgo(),
                    request.getGender(),
                    request.getPlaceOfBirth()
            );

            if(!existingHero.isEmpty()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ja existe um herói com essas caracteristicas");
            }

            Hero refreshedDb = heroRepository.save(heroDb);

            return new HeroResponse(refreshedDb.getId(), refreshedDb.getName(),
                    refreshedDb.getAlterEgo(),
                    refreshedDb.getRace(),
                    refreshedDb.getGender(),
                    refreshedDb.getPlaceOfBirth(),
                    refreshedDb.getImageUrl(),
                    refreshedDb.getIntelligence(),
                    refreshedDb.getStrength(),
                    refreshedDb.getSpeed(),
                    refreshedDb.getPower());
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Herói não encontrado");
    }

    @CacheEvict(value="heroes", allEntries = true)
    public void deleteFromDb(Long id) {
        var heroDb = heroRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Herói não encontrado com o id: " + id)
        );
        heroRepository.delete(heroDb);
    }
}
