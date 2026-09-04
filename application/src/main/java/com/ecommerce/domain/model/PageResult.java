package com.ecommerce.domain.model;

import java.util.List;
import java.util.function.Function;

public record PageResult<T>(
    List<T> content,
    int pageNumber,
    int pageSize,
    long totalElements,
    int totalPages,
    boolean isFirst,
    boolean isLast
) {
  public static <T> PageResult<T> of(
      List<T> content,
      int pageNumber,
      int pageSize,
      long totalElements
  ) {
    int totalPages = pageSize > 0 ? (int) Math.ceil((double) totalElements / pageSize) : 0;
    boolean isFirst = pageNumber <= 0;
    boolean isLast = pageNumber >= totalPages - 1;
    return new PageResult<>(
        content != null ? content : List.of(),
        pageNumber,
        pageSize,
        totalElements,
        totalPages,
        isFirst,
        isLast
    );
  }

  public <R> PageResult<R> map(Function<T, R> mapper) {
    List<R> mappedContent = this.content.stream().map(mapper).toList();
    return new PageResult<>(
        mappedContent,
        this.pageNumber,
        this.pageSize,
        this.totalElements,
        this.totalPages,
        this.isFirst,
        this.isLast
    );
  }
}

