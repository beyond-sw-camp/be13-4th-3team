package com.beyond3.yyGang.hfunction;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QHFunctionalItem is a Querydsl query type for HFunctionalItem
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHFunctionalItem extends EntityPathBase<HFunctionalItem> {

    private static final long serialVersionUID = -1905054708L;

    public static final QHFunctionalItem hFunctionalItem = new QHFunctionalItem("hFunctionalItem");

    public final NumberPath<Long> healthId = createNumber("healthId", Long.class);

    public final EnumPath<HFunctionName> healthName = createEnum("healthName", HFunctionName.class);

    public QHFunctionalItem(String variable) {
        super(HFunctionalItem.class, forVariable(variable));
    }

    public QHFunctionalItem(Path<? extends HFunctionalItem> path) {
        super(path.getType(), path.getMetadata());
    }

    public QHFunctionalItem(PathMetadata metadata) {
        super(HFunctionalItem.class, metadata);
    }

}

