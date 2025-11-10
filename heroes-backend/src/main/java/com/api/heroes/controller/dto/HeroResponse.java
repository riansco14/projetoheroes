package com.api.heroes.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class HeroResponse {
    private Long id;
    private String name;
    private String alterEgo;
    private String race;
    private String gender;
    private String placeOfBirth;
    private String imageUrl;
    private Integer intelligence;
    private Integer strength;
    private Integer speed;
    private Integer power;

    

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        HeroResponse that = (HeroResponse) o;
        return Objects.equals(getName(), that.getName()) && Objects.equals(getAlterEgo(), that.getAlterEgo()) && Objects.equals(getGender(), that.getGender()) && Objects.equals(getPlaceOfBirth(), that.getPlaceOfBirth());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getAlterEgo(), getGender(), getPlaceOfBirth());
    }
}
