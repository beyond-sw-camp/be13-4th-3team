package com.beyond3.yyGang.nsupplement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NSupplementSearchRequestDtoV2 {

    private String productName;

    private List<Long> healthIds = new ArrayList<>();

    private List<Long> ingredientIds = new ArrayList<>();

    private String sortType;
}
