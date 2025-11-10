package com.api.heroes.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public interface HeroApiClient {

    @GetMapping
    List<HeroApiExtResponseDto> getHeroes();
}
