package com.beyond3.yyGang.review.domain;

import com.beyond3.yyGang.EntityDate;
import com.beyond3.yyGang.nsupplement.NSupplement;
import com.beyond3.yyGang.user.domain.User;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "review")
public class Review extends EntityDate {

    // 상품 리뷰 테이블
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;  // 리뷰 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 회원 - 외래키

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "products_id")
    private NSupplement nSupplement;  // 상품 ID - 외래키

    @Column(columnDefinition = "TEXT")
    private String content; // 리뷰 내용

    public void setContent(String content) {
        // content 값이 null이나 빈 문자열이 아닌 경우에만 업데이트하도록
        if(StringUtils.isNotEmpty(content)){
            this.content = content;
        }
    }
}
