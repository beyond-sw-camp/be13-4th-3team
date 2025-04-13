package com.beyond3.yyGang.hfunction;

import com.beyond3.yyGang.nsupplement.NSupplement;
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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "h_functional_category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HFunctionalCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hfunc_id")
    private Long hfuncId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "products_id")
    private NSupplement nSupplement;  // 상품ID 외래키

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "health_id")
    private HFunctionalItem hFunctionalItem; // 건강 기능 ID 외래키

    public HFunctionalCategory(NSupplement nSupplement, HFunctionalItem hFunctionalItem) {
        this.nSupplement = nSupplement;
        this.hFunctionalItem = hFunctionalItem;
    }
}
