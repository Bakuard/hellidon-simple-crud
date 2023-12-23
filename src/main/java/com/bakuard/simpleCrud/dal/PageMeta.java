package com.bakuard.simpleCrud.dal;

public record PageMeta(long pageSize, long pageNumber, long totalItems) {
    public static final long MAX_PAGE_SIZE = 100L;

    public PageMeta(long pageSize, long pageNumber, long totalItems) {
        this.pageSize = Math.min(Math.max(1, pageSize), MAX_PAGE_SIZE);
        this.totalItems = Math.max(0, totalItems);
        this.pageNumber = Math.min(Math.max(0, pageNumber), lastPageNumber());
    }

    public long lastPageNumber() {
        return totalItems == 0 ? 0 : (totalItems - 1) / pageSize;
    }

    public long totalPages() {
        return totalItems == 0 ? 0 : totalItems / pageSize + 1;
    }

    public long offset() {
        return pageNumber * pageSize;
    }

    public boolean isEmpty() {
        return totalItems == 0;
    }
}
