package com.api.heroes.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.api.heroes.controller.dto.*;
import com.api.heroes.service.HeroService;

@RestController
@RequestMapping("/heroes")
public class HeroController {

    @Autowired
    private HeroService heroService;

    @GetMapping
    public HeroResponsePaginationDto getHeroesPerPagination(@RequestParam(required = false) String heroName,
                                                            @RequestParam(defaultValue = "0") Integer page,
                                                            @RequestParam(defaultValue = "2") Integer sizePage) {

        return heroService.getHeroesWithPagination(heroName, page, sizePage);
    }

    @PostMapping
    public ResponseEntity<HeroResponse> addHero(@Valid @RequestBody HeroRequest request) {
        HeroResponse heroResponse = heroService.addHero(request);

        return new ResponseEntity<>(heroResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public HeroResponse getHeroByCode(@PathVariable Long id) {

        return heroService.getHeroByCode(id);
    }

    @PutMapping("/{id}")
    public HeroResponse refreshHero(@PathVariable Long id, @RequestBody HeroRequest request) {
        return heroService.refreshDb(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHero(@PathVariable Long id) {
        heroService.deleteFromDb(id);

        return ResponseEntity.noContent().build();
    }

}
