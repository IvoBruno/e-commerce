package com.ecommerce.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ecommerce.domain.model.PageResult;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PageResultTest {

  @Test
  @DisplayName("Should correctly compute total pages, isFirst, and isLast")
  void shouldComputePaginationMetadata() {
    List<String> items = List.of("A", "B", "C", "D", "E");
    PageResult<String> page0 = PageResult.of(items, 0, 5, 15);

    assertEquals(0, page0.pageNumber());
    assertEquals(5, page0.pageSize());
    assertEquals(15, page0.totalElements());
    assertEquals(3, page0.totalPages());
    assertTrue(page0.isFirst());
    assertFalse(page0.isLast());

    PageResult<String> page2 = PageResult.of(items, 2, 5, 15);
    assertEquals(2, page2.pageNumber());
    assertFalse(page2.isFirst());
    assertTrue(page2.isLast());
  }

  @Test
  @DisplayName("Should transform content with map function while keeping metadata intact")
  void shouldMapContentPreservingMetadata() {
    List<Integer> numbers = List.of(1, 2, 3);
    PageResult<Integer> page = PageResult.of(numbers, 0, 10, 3);

    PageResult<String> mapped = page.map(n -> "Item " + n);

    assertEquals(List.of("Item 1", "Item 2", "Item 3"), mapped.content());
    assertEquals(0, mapped.pageNumber());
    assertEquals(10, mapped.pageSize());
    assertEquals(3, mapped.totalElements());
    assertEquals(1, mapped.totalPages());
    assertTrue(mapped.isFirst());
    assertTrue(mapped.isLast());
  }

  @Test
  @DisplayName("Should handle empty content gracefully")
  void shouldHandleEmptyContent() {
    PageResult<String> emptyPage = PageResult.of(List.of(), 0, 10, 0);

    assertTrue(emptyPage.content().isEmpty());
    assertEquals(0, emptyPage.totalElements());
    assertEquals(0, emptyPage.totalPages());
    assertTrue(emptyPage.isFirst());
    assertTrue(emptyPage.isLast());
  }
}

