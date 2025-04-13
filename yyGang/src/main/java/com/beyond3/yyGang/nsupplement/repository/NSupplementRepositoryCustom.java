package com.beyond3.yyGang.nsupplement.repository;

import com.beyond3.yyGang.nsupplement.dto.NSupplementResponseDtoV2;
import com.beyond3.yyGang.nsupplement.dto.NSupplementSearchRequestDtoV2;
import com.beyond3.yyGang.nsupplement.dto.PageResponseDto;
import org.springframework.data.domain.Pageable;


public interface NSupplementRepositoryCustom {

//    PageResponseDto<NSupplementResponseDto> searchPage(NSupplementSearchRequestDto searchRequest, Pageable pageable, SortType sortType);

    PageResponseDto<NSupplementResponseDtoV2> searchPageV2(NSupplementSearchRequestDtoV2 searchRequest, Pageable pageable, SortType sortType);
}
