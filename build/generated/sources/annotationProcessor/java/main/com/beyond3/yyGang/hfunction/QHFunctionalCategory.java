package com.beyond3.yyGang.hfunction;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QHFunctionalCategory is a Querydsl query type for HFunctionalCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHFunctionalCategory extends EntityPathBase<HFunctionalCategory> {

    private static final long serialVersionUID = 1184975223L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QHFunctionalCategory hFunctionalCategory = new QHFunctionalCategory("hFunctionalCategory");

    public final NumberPath<Long> hfuncId = createNumber("hfuncId", Long.class);

    public final QHFunctionalItem hFunctionalItem;

    public final com.beyond3.yyGang.nsupplement.QNSupplement nSupplement;

    public QHFunctionalCategory(String variable) {
        this(HFunctionalCategory.class, forVariable(variable), INITS);
    }

    public QHFunctionalCategory(Path<? extends HFunctionalCategory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QHFunctionalCategory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QHFunctionalCategory(PathMetadata metadata, PathInits inits) {
        this(HFunctionalCategory.class, metadata, inits);
    }

    public QHFunctionalCategory(Class<? extends HFunctionalCategory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.hFunctionalItem = inits.isInitialized("hFunctionalItem") ? new QHFunctionalItem(forProperty("hFunctionalItem")) : null;
        this.nSupplement = inits.isInitialized("nSupplement") ? new com.beyond3.yyGang.nsupplement.QNSupplement(forProperty("nSupplement"), inits.get("nSupplement")) : null;
    }

}

