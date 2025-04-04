package com.beyond3.yyGang.ingredient.dto;

import com.beyond3.yyGang.ingredient.domain.IngredientName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngredientResponseDto {

    private Map<Long, IngredientName> ingredientMap = new HashMap<>();
}
