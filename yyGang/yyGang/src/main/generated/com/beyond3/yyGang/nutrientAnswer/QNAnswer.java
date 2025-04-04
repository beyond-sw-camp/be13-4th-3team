package com.beyond3.yyGang.nutrientAnswer;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNAnswer is a Querydsl query type for NAnswer
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNAnswer extends EntityPathBase<NAnswer> {

    private static final long serialVersionUID = 431368487L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNAnswer nAnswer = new QNAnswer("nAnswer");

    public final com.beyond3.yyGang.QEntityDate _super = new com.beyond3.yyGang.QEntityDate(this);

    public final StringPath aContent = createString("aContent");

    public final NumberPath<Long> answerId = createNumber("answerId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final com.beyond3.yyGang.nutrientQuestion.QNQuestion question;

    public final com.beyond3.yyGang.user.domain.QUser user;

    public QNAnswer(String variable) {
        this(NAnswer.class, forVariable(variable), INITS);
    }

    public QNAnswer(Path<? extends NAnswer> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNAnswer(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNAnswer(PathMetadata metadata, PathInits inits) {
        this(NAnswer.class, metadata, inits);
    }

    public QNAnswer(Class<? extends NAnswer> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.question = inits.isInitialized("question") ? new com.beyond3.yyGang.nutrientQuestion.QNQuestion(forProperty("question"), inits.get("question")) : null;
        this.user = inits.isInitialized("user") ? new com.beyond3.yyGang.user.domain.QUser(forProperty("user")) : null;
    }

}

