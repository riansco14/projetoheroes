package com.api.heroes.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HeroResponsePaginationDto {
    //paginação info
    private Integer totalPaginas;
    private Long totalElementos;
    private Integer quantidadeItensPorPagina;
    private Integer pagina;
    //conteudo da consulta
    private List<HeroResponse> heroes;
}
