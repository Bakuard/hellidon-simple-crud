package com.bakuard.simpleCrud.dal;

public record PageMeta(int pageSize, int pageNumber, int totalItems) {
    public PageMeta(int pageSize, int pageNumber, int totalItems) {
        this.pageSize = Math.max(1, pageSize);
        this.pageNumber = Math.max(0, pageNumber);
        this.totalItems = Math.max(0, totalItems);
    }

    public int totalPages() {
        return totalItems == 0 ? 0 : totalItems / pageSize + 1;
    }

    public int offset() {
        return pageNumber * pageSize;
    }
}
