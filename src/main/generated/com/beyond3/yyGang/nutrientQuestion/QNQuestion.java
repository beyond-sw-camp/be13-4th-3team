package com.beyond3.yyGang.nutrientQuestion;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNQuestion is a Querydsl query type for NQuestion
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNQuestion extends EntityPathBase<NQuestion> {

    private static final long serialVersionUID = -576828809L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNQuestion nQuestion = new QNQuestion("nQuestion");

    public final com.beyond3.yyGang.QEntityDate _super = new com.beyond3.yyGang.QEntityDate(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath qContent = createString("qContent");

    public final NumberPath<Long> questionId = createNumber("questionId", Long.class);

    public final com.beyond3.yyGang.nsupplement.QNSupplement supplement;

    public final com.beyond3.yyGang.user.domain.QUser user;

    public QNQuestion(String variable) {
        this(NQuestion.class, forVariable(variable), INITS);
    }

    public QNQuestion(Path<? extends NQuestion> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNQuestion(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNQuestion(PathMetadata metadata, PathInits inits) {
        this(NQuestion.class, metadata, inits);
    }

    public QNQuestion(Class<? extends NQuestion> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.supplement = inits.isInitialized("supplement") ? new com.beyond3.yyGang.nsupplement.QNSupplement(forProperty("supplement"), inits.get("supplement")) : null;
        this.user = inits.isInitialized("user") ? new com.beyond3.yyGang.user.domain.QUser(forProperty("user")) : null;
    }

}

