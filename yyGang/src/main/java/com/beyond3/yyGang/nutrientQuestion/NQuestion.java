package com.beyond3.yyGang.nutrientQuestion;

import com.beyond3.yyGang.EntityDate;
import com.beyond3.yyGang.nsupplement.NSupplement;
import com.beyond3.yyGang.user.domain.User;
import io.micrometer.common.util.StringUtils;
import io.netty.util.internal.StringUtil;
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
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "n_question")
public class NQuestion extends EntityDate {
    // 영양제 관련 문의사항

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long questionId; // 문의 사항 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "products_id")
    private NSupplement supplement;

    @Column(columnDefinition = "TEXT")
    private String qContent; // 질문 내용

//    @OneToOne(mappedBy = "question")
//    private NAnswer nAnswer;

    public void update(String answerContent) {
        if(StringUtils.isNotBlank(answerContent)) {
            qContent = answerContent;
        }
    }
}
