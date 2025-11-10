package com.api.heroes.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeroApiExtResponseDto {
    private Long id;
    private String name;
    private String alterEgo;
    private String gender;
    private String placeOfBirth;

}
