package com.api.heroes.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.api.heroes.model.Hero;

import java.util.List;
import java.util.Optional;

@Repository
public interface HeroRepository extends JpaRepository<Hero, Long> {

    //query methods
    List<Hero> findAllByName(String nome);

    //query com JPQL
    @Query("SELECT h FROM Hero h WHERE h.name = :nameParam")
    Hero findByName(@Param("nomeParam") String nameParam);

    //query nativa
    //@Query(value = "SELECT * FROM bancos b WHERE b.nome = :nomeParam", nativeQuery = true)
    //Hero findByNome(@Param("nomeParam") String nomeParam);

    Optional<Hero> findById(Long id);

    @Query("SELECT h FROM Hero h WHERE :heroName IS NULL OR h.name = :heroName")
    Page<Hero> findByNameWithPagination(@Param("heroName") String heroName, Pageable pageable);

    @Query( "SELECT h FROM Hero h WHERE "        +
            "h.name = :name AND "                +
            "h.alterEgo = :alterEgo AND "        +
            "h.gender = :gender AND "           +
            "h.placeOfBirth = :placeOfBirth"    )
    List<Hero> findAnyEqualHero(@Param("name") String name,
                                @Param("alterEgo") String alterEgo,
                                @Param("gender") String gender,
                                @Param("placeOfBirth") String placeOfBirth);




}
