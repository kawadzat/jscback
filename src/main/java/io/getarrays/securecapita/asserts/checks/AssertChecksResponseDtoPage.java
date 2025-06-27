package io.getarrays.securecapita.asserts.checks;

import org.springframework.data.domain.Page;

import java.sql.Timestamp;
import java.util.List;


public record AssertChecksResponseDtoPage(
        int size,
        int page,
        boolean next,
        boolean previous,
        Long total,
        List<AssertChecksResponseDto> checksList
) {
    public AssertChecksResponseDtoPage(Page<AssertChecksResponseDto> pageData) {
        this(pageData.getSize(),
                pageData.getNumber(),
                pageData.hasNext(),
                pageData.hasPrevious(),
                pageData.getTotalElements(),
                pageData.toList());
    }
}

