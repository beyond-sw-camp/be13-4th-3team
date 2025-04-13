package com.beyond3.yyGang.nsupplement;

import com.beyond3.yyGang.handler.exception.NSupplementException;
import com.beyond3.yyGang.handler.message.ExceptionMessage;
import com.beyond3.yyGang.nsupplement.dto.NSupplementModifyDto;
import com.beyond3.yyGang.nsupplement.dto.NSupplementRegisterDto;
import com.beyond3.yyGang.review.domain.Review;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@Entity
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "n_supplement")
public class NSupplement {

    // 영양제
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;  // 상품 아이디

    private String productName; // 상품 이름

    @Column(columnDefinition = "TEXT")
    private String caution; // 주의 사항

    private String brand;  // 브랜드

    private int price;  // 상품 가격

    private int stockQuantity;

    private int reviewCount;    // 전체 리뷰 수

    private String productImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private User seller;

    @OneToMany(mappedBy = "nSupplement")
    private List<Review> reviews;       // 이거 왜 넣었더라

    public void updateNSupplement(NSupplementModifyDto dto) {
        // null이거나 값이 비어 있는 경우는 업데이트 안되게ㅇㅇ

        if(StringUtils.isNotBlank(dto.getProductName())){
            this.productName = dto.getProductName();
        }

        if(StringUtils.isNotBlank(dto.getCaution())){
            this.caution = dto.getCaution();
        }

        if(StringUtils.isNotBlank(dto.getBrand())){
            this.brand = dto.getBrand();
        }

//        if(StringUtils.isNotBlank(dto.getProductImage())){
//            this.productImage = dto.getProductImage();
//        }
        this.productImage = dto.getProductImage();

        Optional.of(dto.getPrice()).ifPresent(this::setPrice);
        Optional.of(dto.getStockQuantity()).ifPresent(this::setStockQuantity);
    }

    public NSupplementRegisterDto toDto(){
        return NSupplementRegisterDto.builder()
                .productName(productName)
                .brand(this.brand)
                .caution(this.caution)
                .price(this.price)
                .stockQuantity(this.stockQuantity)
                .productImage(this.productImage)
                .build();
    }

    public void decreaseStockQuantity(int quantity){
        if(stockQuantity - quantity < 0){
            throw new NSupplementException(ExceptionMessage.OUT_OF_STOCK);
        }
        this.stockQuantity -= quantity;
    }

    public void increaseStockQuantity(int quantity){
        this.stockQuantity += quantity;
    }
}
