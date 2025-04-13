package com.beyond3.yyGang.user.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -1918159338L;

    public static final QUser user = new QUser("user");

    public final StringPath address = createString("address");

    public final DatePath<java.time.LocalDate> birthday = createDate("birthday", java.time.LocalDate.class);

    public final DateTimePath<java.time.LocalDateTime> createdDate = createDateTime("createdDate", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final EnumPath<Gender> gender = createEnum("gender", Gender.class);

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final StringPath phone = createString("phone");

    public final ListPath<com.beyond3.yyGang.review.domain.Review, com.beyond3.yyGang.review.domain.QReview> reviews = this.<com.beyond3.yyGang.review.domain.Review, com.beyond3.yyGang.review.domain.QReview>createList("reviews", com.beyond3.yyGang.review.domain.Review.class, com.beyond3.yyGang.review.domain.QReview.class, PathInits.DIRECT2);

    public final EnumPath<Role_name> role = createEnum("role", Role_name.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

