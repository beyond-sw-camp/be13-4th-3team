package com.beyond3.yyGang.nutrientAnswer;

import com.beyond3.yyGang.EntityDate;
import com.beyond3.yyGang.nutrientAnswer.dto.NutrientAnswerModifyDto;
import com.beyond3.yyGang.nutrientQuestion.NQuestion;
import com.beyond3.yyGang.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
@Table(name = "n_answer")
public class NAnswer extends EntityDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long answerId;  // 답변 ID

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private NQuestion question; // 질문 ID - 외래키

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private User user; // 대답자 ID - 판매자 ID

    @Column(columnDefinition = "TEXT")
    private String aContent; // 응답 내용

    public void update(NutrientAnswerModifyDto dto) {
        this.aContent = dto.getAnswerContent();
    }
}
