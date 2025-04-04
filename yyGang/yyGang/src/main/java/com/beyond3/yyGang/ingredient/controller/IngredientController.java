package com.beyond3.yyGang.ingredient.controller;


import com.beyond3.yyGang.ingredient.domain.Ingredient;
import com.beyond3.yyGang.ingredient.domain.IngredientName;
import com.beyond3.yyGang.ingredient.dto.IngredientResponseDto;
import com.beyond3.yyGang.ingredient.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/ingredients")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientRepository ingredientRepository;

    @GetMapping("")
    public ResponseEntity<IngredientResponseDto> ingredients() {
        Map<Long, IngredientName> findAllMap = ingredientRepository.findAll().stream().collect(Collectors.toMap(Ingredient::getIngredientID, Ingredient::getIngredientName));
        IngredientResponseDto ingredientResponseDto = new IngredientResponseDto(findAllMap);

        return ResponseEntity.ok(ingredientResponseDto);
    }
}
