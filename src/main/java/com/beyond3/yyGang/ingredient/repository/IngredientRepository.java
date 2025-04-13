package com.beyond3.yyGang.ingredient.repository;

import com.beyond3.yyGang.ingredient.domain.Ingredient;
import com.beyond3.yyGang.ingredient.domain.IngredientName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    Optional<Ingredient> findByingredientName(IngredientName ingredientName);
}
