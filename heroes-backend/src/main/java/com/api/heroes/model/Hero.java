package com.api.heroes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "superHeros")
public class Hero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String alterEgo;

    @Column
    private String race;

    @Column
    private String gender;

    @Column
    private String placeOfBirth;

    @Column
    private String imageUrl;

    @Column
    private Integer intelligence;

    @Column
    private Integer strength;

    @Column
    private Integer speed;

    @Column
    private Integer power;



    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Hero hero = (Hero) o;
        return Objects.equals(getName(), hero.getName()) && Objects.equals(getAlterEgo(), hero.getAlterEgo()) && Objects.equals(getGender(), hero.getGender()) && Objects.equals(getPlaceOfBirth(), hero.getPlaceOfBirth());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getAlterEgo(), getGender(), getPlaceOfBirth());
    }
}
