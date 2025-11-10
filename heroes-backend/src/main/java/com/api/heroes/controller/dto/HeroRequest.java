package com.api.heroes.controller.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeroRequest {
    @NotBlank(message = "O nome de herói é obrigatório")
    private String name;
    @NotBlank(message = "A identidade do herói é obrigatória")
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
        HeroRequest that = (HeroRequest) o;
        return Objects.equals(getName(), that.getName()) && Objects.equals(getAlterEgo(), that.getAlterEgo()) && Objects.equals(getGender(), that.getGender()) && Objects.equals(getPlaceOfBirth(), that.getPlaceOfBirth());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getAlterEgo(), getGender(), getPlaceOfBirth());
    }
}
