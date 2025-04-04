package com.beyond3.yyGang.cart.domain;

import com.beyond3.yyGang.user.domain.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Table(name = "cart")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long cartId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // CascadeType = Cart 에 대한 수정 삭제 -> CartOption에도 적용된다는 의미
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartOption> cartOptions;

    @Builder
    private Cart(User user) {
        this.user = user;
    }

    // 장바구니는 회원가입했을 때 생성되어야 할 듯
    public static Cart createCart(User user) {
        return new Cart(user);
    }
}
