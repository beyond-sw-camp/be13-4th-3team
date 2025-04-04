package com.beyond3.yyGang.q_board;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQuestionBoard is a Querydsl query type for QuestionBoard
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QQuestionBoard extends EntityPathBase<QuestionBoard> {

    private static final long serialVersionUID = -2014962106L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQuestionBoard questionBoard = new QQuestionBoard("questionBoard");

    public final com.beyond3.yyGang.QEntityDate _super = new com.beyond3.yyGang.QEntityDate(this);

    public final ListPath<com.beyond3.yyGang.answer.domain.Answer, com.beyond3.yyGang.answer.domain.QAnswer> answers = this.<com.beyond3.yyGang.answer.domain.Answer, com.beyond3.yyGang.answer.domain.QAnswer>createList("answers", com.beyond3.yyGang.answer.domain.Answer.class, com.beyond3.yyGang.answer.domain.QAnswer.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath qboardContent = createString("qboardContent");

    public final NumberPath<Long> qboardId = createNumber("qboardId", Long.class);

    public final StringPath qboardTitle = createString("qboardTitle");

    public final com.beyond3.yyGang.user.domain.QUser user;

    public final NumberPath<Integer> viewCount = createNumber("viewCount", Integer.class);

    public QQuestionBoard(String variable) {
        this(QuestionBoard.class, forVariable(variable), INITS);
    }

    public QQuestionBoard(Path<? extends QuestionBoard> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQuestionBoard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQuestionBoard(PathMetadata metadata, PathInits inits) {
        this(QuestionBoard.class, metadata, inits);
    }

    public QQuestionBoard(Class<? extends QuestionBoard> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.beyond3.yyGang.user.domain.QUser(forProperty("user")) : null;
    }

}

