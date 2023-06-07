package com.matching.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PostSearchRequest {
    private String keyword;
    @Nullable
    private int pageNum;
    @Nullable
    private int pageSize;

//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//    private LocalDate startDate;
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//    private LocalDate endDate;
//
    public int getPageSize() {
        return this.pageSize == 0 ? 5 : this.pageSize;
    }

}
