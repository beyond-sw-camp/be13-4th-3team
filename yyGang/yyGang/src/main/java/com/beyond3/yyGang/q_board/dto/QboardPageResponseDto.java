package com.beyond3.yyGang.q_board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class QboardPageResponseDto {
    List<QboardResponseDto> qboardResponseDto;

    private long totalElements;
}
