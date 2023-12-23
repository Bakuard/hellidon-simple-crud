package com.bakuard.simpleCrud.dal;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Итоговый результат применения пагинации.
 * @param meta метаданные страницы (см. {@link PageMeta}).
 * @param data полезные данные содержащиеся в данной странице.
 */
public record Page<T>(PageMeta meta, List<T> data) {

    public Stream<T> stream() {
        return data.stream();
    }

    public <S> Page<S> map(Function<T, S> mapper) {
        return new Page(meta, stream().map(mapper).toList());
    }
}
