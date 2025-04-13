package com.beyond3.yyGang.cart.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCartOption is a Querydsl query type for CartOption
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCartOption extends EntityPathBase<CartOption> {

    private static final long serialVersionUID = 663323093L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCartOption cartOption = new QCartOption("cartOption");

    public final QCart cart;

    public final NumberPath<Long> cartOptionID = createNumber("cartOptionID", Long.class);

    public final com.beyond3.yyGang.nsupplement.QNSupplement nSupplement;

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final NumberPath<Integer> quantity = createNumber("quantity", Integer.class);

    public QCartOption(String variable) {
        this(CartOption.class, forVariable(variable), INITS);
    }

    public QCartOption(Path<? extends CartOption> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCartOption(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCartOption(PathMetadata metadata, PathInits inits) {
        this(CartOption.class, metadata, inits);
    }

    public QCartOption(Class<? extends CartOption> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cart = inits.isInitialized("cart") ? new QCart(forProperty("cart"), inits.get("cart")) : null;
        this.nSupplement = inits.isInitialized("nSupplement") ? new com.beyond3.yyGang.nsupplement.QNSupplement(forProperty("nSupplement"), inits.get("nSupplement")) : null;
    }

}

