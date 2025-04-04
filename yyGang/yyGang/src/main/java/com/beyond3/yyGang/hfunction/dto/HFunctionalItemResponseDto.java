package com.beyond3.yyGang.hfunction.dto;

import com.beyond3.yyGang.hfunction.HFunctionName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HFunctionalItemResponseDto {

    private Map<Long, HFunctionName> healthMap = new HashMap<>();

}
