package com.beyond3.yyGang.nsupplement;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNSupplement is a Querydsl query type for NSupplement
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNSupplement extends EntityPathBase<NSupplement> {

    private static final long serialVersionUID = 1396830844L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNSupplement nSupplement = new QNSupplement("nSupplement");

    public final StringPath brand = createString("brand");

    public final StringPath caution = createString("caution");

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final NumberPath<Long> productId = createNumber("productId", Long.class);

    public final StringPath productImage = createString("productImage");

    public final StringPath productName = createString("productName");

    public final NumberPath<Integer> reviewCount = createNumber("reviewCount", Integer.class);

    public final ListPath<com.beyond3.yyGang.review.domain.Review, com.beyond3.yyGang.review.domain.QReview> reviews = this.<com.beyond3.yyGang.review.domain.Review, com.beyond3.yyGang.review.domain.QReview>createList("reviews", com.beyond3.yyGang.review.domain.Review.class, com.beyond3.yyGang.review.domain.QReview.class, PathInits.DIRECT2);

    public final com.beyond3.yyGang.user.domain.QUser seller;

    public final NumberPath<Integer> stockQuantity = createNumber("stockQuantity", Integer.class);

    public QNSupplement(String variable) {
        this(NSupplement.class, forVariable(variable), INITS);
    }

    public QNSupplement(Path<? extends NSupplement> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNSupplement(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNSupplement(PathMetadata metadata, PathInits inits) {
        this(NSupplement.class, metadata, inits);
    }

    public QNSupplement(Class<? extends NSupplement> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.seller = inits.isInitialized("seller") ? new com.beyond3.yyGang.user.domain.QUser(forProperty("seller")) : null;
    }

}

