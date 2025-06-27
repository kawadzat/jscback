package io.getarrays.securecapita.domain;

import lombok.Getter;

import java.util.List;

@Getter
public class PageResponseDto <T> {

    private List<T> content; // The list of entities or DTOs on the current page

    private long totalElements; // Total number of elements across all pages

    private int totalPages; // Total number of pages

    private int currentPage; // Current page number (starting from 0)

    private int pageSize; // Number of items per page

    private boolean first; // Whether the current page is the first page

    private boolean last; // Whether the current page is the last page

    private boolean empty; // Whether the page is empty


    public PageResponseDto(List<T> content, org.springframework.data.domain.Page page) {
        this.content = content;
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.currentPage = page.getNumber();
        this.pageSize = page.getSize();
        this.first = page.isFirst();
        this.last = page.isLast();
        this.empty = page.isEmpty();
    }
}