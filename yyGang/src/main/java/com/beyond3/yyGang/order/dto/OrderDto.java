package com.beyond3.yyGang.order.dto;

import com.beyond3.yyGang.nsupplement.NSupplement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private long supplement_id; // 영양제 아이디

    private int count;  // 영양제 수량
}
