package com.bakuard.simpleCrud.dal;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public record Page<T>(PageMeta meta, List<T> data) {

    public Stream<T> stream() {
        return data.stream();
    }

    public <S> Page<S> map(Function<T, S> mapper) {
        return new Page(meta, stream().map(mapper).toList());
    }
}
