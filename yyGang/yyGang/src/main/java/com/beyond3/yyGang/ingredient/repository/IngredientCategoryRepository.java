package com.beyond3.yyGang.ingredient.repository;

import com.beyond3.yyGang.ingredient.domain.IngredientCategory;
import com.beyond3.yyGang.nsupplement.NSupplement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngredientCategoryRepository extends JpaRepository<IngredientCategory, Long> {

    List<IngredientCategory> findBynSupplement(NSupplement nSupplement);

}
