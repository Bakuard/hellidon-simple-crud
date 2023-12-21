package com.bakuard.simpleCrud.dal;

import java.util.List;

public record Page<T>(int pageSize,
                      int pageNumber,
                      int totalPages,
                      int totalItems,
                      List<T> data) {}
