package com.api.heroes.configs;

import com.api.heroes.model.Hero;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Teste {
    private static final ObjectMapper mapper = new ObjectMapper();

    private static  <T> T readJson(String classpathPath, TypeReference<T> type) throws Exception {
        ClassPathResource res = new ClassPathResource(classpathPath);
        try (InputStream is = res.getInputStream()) {
            byte[] bytes = is.readAllBytes();
            return mapper.readValue(new String(bytes, StandardCharsets.UTF_8), type);
        }
    }

    public static void main(String[] args) throws Exception {
        List<Hero> heroes = readJson("seed/heroes.json", new TypeReference<>() {});
        System.out.println(heroes.get(730));
    }
}
