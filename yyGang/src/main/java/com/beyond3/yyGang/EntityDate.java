package com.beyond3.yyGang;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)  // 엔티티 수정 시 자동으로 생성일자, 수정 일자를 채워주는 기능 제공
@MappedSuperclass   // 얘는 JPA 엔티티로 직접 저장되지는 않음 -> 얘를 상속 받은 애들이 사용하도록 설정해야 함
@Getter
public abstract class EntityDate {

    @CreatedDate    // 엔티티 최초 생성 시 생성 일자를 자동으로 채워줌
    @Column(nullable = false, updatable = false)    // null 값 받을 수 없음 / update도 할 수 없음
    private LocalDateTime createdAt;

    @LastModifiedDate   // 엔티티 수정 시 수정 일자를 자동으로 채워줌
    private LocalDateTime modifiedAt;
}
