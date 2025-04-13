package com.beyond3.yyGang.answer.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAnswerLike is a Querydsl query type for AnswerLike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAnswerLike extends EntityPathBase<AnswerLike> {

    private static final long serialVersionUID = -1076433229L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAnswerLike answerLike = new QAnswerLike("answerLike");

    public final QAnswer answer;

    public final QAnswerLikedId id;

    public final com.beyond3.yyGang.user.domain.QUser user;

    public QAnswerLike(String variable) {
        this(AnswerLike.class, forVariable(variable), INITS);
    }

    public QAnswerLike(Path<? extends AnswerLike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAnswerLike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAnswerLike(PathMetadata metadata, PathInits inits) {
        this(AnswerLike.class, metadata, inits);
    }

    public QAnswerLike(Class<? extends AnswerLike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.answer = inits.isInitialized("answer") ? new QAnswer(forProperty("answer"), inits.get("answer")) : null;
        this.id = inits.isInitialized("id") ? new QAnswerLikedId(forProperty("id")) : null;
        this.user = inits.isInitialized("user") ? new com.beyond3.yyGang.user.domain.QUser(forProperty("user")) : null;
    }

}

