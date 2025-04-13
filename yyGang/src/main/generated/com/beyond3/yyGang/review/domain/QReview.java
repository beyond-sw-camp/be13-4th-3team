package com.beyond3.yyGang.review.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReview is a Querydsl query type for Review
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReview extends EntityPathBase<Review> {

    private static final long serialVersionUID = -1495088464L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReview review = new QReview("review");

    public final com.beyond3.yyGang.QEntityDate _super = new com.beyond3.yyGang.QEntityDate(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final com.beyond3.yyGang.nsupplement.QNSupplement nSupplement;

    public final NumberPath<Long> reviewId = createNumber("reviewId", Long.class);

    public final com.beyond3.yyGang.user.domain.QUser user;

    public QReview(String variable) {
        this(Review.class, forVariable(variable), INITS);
    }

    public QReview(Path<? extends Review> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReview(PathMetadata metadata, PathInits inits) {
        this(Review.class, metadata, inits);
    }

    public QReview(Class<? extends Review> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.nSupplement = inits.isInitialized("nSupplement") ? new com.beyond3.yyGang.nsupplement.QNSupplement(forProperty("nSupplement"), inits.get("nSupplement")) : null;
        this.user = inits.isInitialized("user") ? new com.beyond3.yyGang.user.domain.QUser(forProperty("user")) : null;
    }

}

