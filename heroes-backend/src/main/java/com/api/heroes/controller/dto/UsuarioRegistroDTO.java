package com.api.heroes.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UsuarioRegistroDTO {
    private String nome;

    private String login;

    private String senha;
}
