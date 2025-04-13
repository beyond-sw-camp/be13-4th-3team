package com.beyond3.yyGang.personalHealth;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPersonalHealth is a Querydsl query type for PersonalHealth
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPersonalHealth extends EntityPathBase<PersonalHealth> {

    private static final long serialVersionUID = -2070046230L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPersonalHealth personalHealth = new QPersonalHealth("personalHealth");

    public final com.beyond3.yyGang.QEntityDate _super = new com.beyond3.yyGang.QEntityDate(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final BooleanPath surComplete = createBoolean("surComplete");

    public final NumberPath<Long> surveyId = createNumber("surveyId", Long.class);

    public final com.beyond3.yyGang.user.domain.QUser user;

    public QPersonalHealth(String variable) {
        this(PersonalHealth.class, forVariable(variable), INITS);
    }

    public QPersonalHealth(Path<? extends PersonalHealth> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPersonalHealth(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPersonalHealth(PathMetadata metadata, PathInits inits) {
        this(PersonalHealth.class, metadata, inits);
    }

    public QPersonalHealth(Class<? extends PersonalHealth> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.beyond3.yyGang.user.domain.QUser(forProperty("user")) : null;
    }

}

