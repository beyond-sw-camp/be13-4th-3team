package com.beyond3.yyGang.ingredient.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QIngredientCategory is a Querydsl query type for IngredientCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QIngredientCategory extends EntityPathBase<IngredientCategory> {

    private static final long serialVersionUID = 466455680L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QIngredientCategory ingredientCategory = new QIngredientCategory("ingredientCategory");

    public final NumberPath<Long> iCategoryId = createNumber("iCategoryId", Long.class);

    public final QIngredient ingredient;

    public final com.beyond3.yyGang.nsupplement.QNSupplement nSupplement;

    public QIngredientCategory(String variable) {
        this(IngredientCategory.class, forVariable(variable), INITS);
    }

    public QIngredientCategory(Path<? extends IngredientCategory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QIngredientCategory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QIngredientCategory(PathMetadata metadata, PathInits inits) {
        this(IngredientCategory.class, metadata, inits);
    }

    public QIngredientCategory(Class<? extends IngredientCategory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.ingredient = inits.isInitialized("ingredient") ? new QIngredient(forProperty("ingredient")) : null;
        this.nSupplement = inits.isInitialized("nSupplement") ? new com.beyond3.yyGang.nsupplement.QNSupplement(forProperty("nSupplement"), inits.get("nSupplement")) : null;
    }

}

