package com.beyond3.yyGang.ingredient.domain;

import com.beyond3.yyGang.nsupplement.NSupplement;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "ingredient_category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IngredientCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "i_category_id")
    private Long iCategoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "products_id")
    private NSupplement nSupplement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    public IngredientCategory(NSupplement nSupplement, Ingredient ingredient) {
        this.nSupplement = nSupplement;
        this.ingredient = ingredient;
    }
}
