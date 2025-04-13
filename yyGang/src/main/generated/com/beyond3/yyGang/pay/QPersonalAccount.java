package com.beyond3.yyGang.pay;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPersonalAccount is a Querydsl query type for PersonalAccount
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPersonalAccount extends EntityPathBase<PersonalAccount> {

    private static final long serialVersionUID = -884292861L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPersonalAccount personalAccount = new QPersonalAccount("personalAccount");

    public final StringPath accountNumber = createString("accountNumber");

    public final NumberPath<Integer> balance = createNumber("balance", Integer.class);

    public final StringPath bankName = createString("bankName");

    public final NumberPath<Long> pId = createNumber("pId", Long.class);

    public final com.beyond3.yyGang.user.domain.QUser user;

    public QPersonalAccount(String variable) {
        this(PersonalAccount.class, forVariable(variable), INITS);
    }

    public QPersonalAccount(Path<? extends PersonalAccount> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPersonalAccount(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPersonalAccount(PathMetadata metadata, PathInits inits) {
        this(PersonalAccount.class, metadata, inits);
    }

    public QPersonalAccount(Class<? extends PersonalAccount> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.beyond3.yyGang.user.domain.QUser(forProperty("user")) : null;
    }

}

