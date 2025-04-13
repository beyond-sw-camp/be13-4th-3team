package com.beyond3.yyGang.nsupplement.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class PageResponseDto<T> implements Serializable {

    private List<T> content;  // 페이징된 결과
    private long totalElements; // 전체 항목 수
    private int totalPages;  // 전체 페이지 수
    private int currentPage;  // 현재 페이지 번호
    private int pageSize;  // 페이지 크기
    private boolean isFirstPage; // 첫 번째 페이지 여부
    private boolean isLastPage;  // 마지막 페이지 여부


    public PageResponseDto(Page<T> page) {
        this.content = page.getContent();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.currentPage = page.getNumber();
        this.pageSize = page.getSize();
        this.isFirstPage = page.isFirst();
        this.isLastPage = page.isLast();
    }
}
