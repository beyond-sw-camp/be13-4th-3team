package com.beyond3.yyGang.order;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrderOption is a Querydsl query type for OrderOption
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrderOption extends EntityPathBase<OrderOption> {

    private static final long serialVersionUID = -1628702145L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrderOption orderOption = new QOrderOption("orderOption");

    public final com.beyond3.yyGang.nsupplement.QNSupplement nSupplement;

    public final QOrder orderId;

    public final NumberPath<Long> orderOptionId = createNumber("orderOptionId", Long.class);

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final NumberPath<Integer> quantity = createNumber("quantity", Integer.class);

    public QOrderOption(String variable) {
        this(OrderOption.class, forVariable(variable), INITS);
    }

    public QOrderOption(Path<? extends OrderOption> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrderOption(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrderOption(PathMetadata metadata, PathInits inits) {
        this(OrderOption.class, metadata, inits);
    }

    public QOrderOption(Class<? extends OrderOption> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.nSupplement = inits.isInitialized("nSupplement") ? new com.beyond3.yyGang.nsupplement.QNSupplement(forProperty("nSupplement"), inits.get("nSupplement")) : null;
        this.orderId = inits.isInitialized("orderId") ? new QOrder(forProperty("orderId"), inits.get("orderId")) : null;
    }

}

