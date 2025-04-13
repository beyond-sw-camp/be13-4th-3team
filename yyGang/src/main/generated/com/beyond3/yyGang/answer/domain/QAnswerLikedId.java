package com.beyond3.yyGang.answer.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAnswerLikedId is a Querydsl query type for AnswerLikedId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QAnswerLikedId extends BeanPath<AnswerLikedId> {

    private static final long serialVersionUID = -1796394740L;

    public static final QAnswerLikedId answerLikedId = new QAnswerLikedId("answerLikedId");

    public final NumberPath<Long> answerId = createNumber("answerId", Long.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QAnswerLikedId(String variable) {
        super(AnswerLikedId.class, forVariable(variable));
    }

    public QAnswerLikedId(Path<? extends AnswerLikedId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAnswerLikedId(PathMetadata metadata) {
        super(AnswerLikedId.class, metadata);
    }

}

