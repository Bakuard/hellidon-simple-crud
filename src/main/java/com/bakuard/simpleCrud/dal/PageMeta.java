package com.bakuard.simpleCrud.dal;

/**
 * Используется для пагинации. Метаданные подготавливаемой страницы. Выделены в отдельный класс,
 * т.к. до получения итоговых данных и в момент формирования SQL запроса удобно иметь класс
 * берущий на себя обязанности по расчету кол-ва страниц, вычислению offset и т.д. Объекты
 * данного класса создаются реализациями репозиториев для облегчения некоторых предварительных рассчетов.
 * @param pageSize размер страницы. Принимает значение от 1 до 100 (включительно). Если заданное значение
 *                 этого параметра выходит за пределы диапазона, оно автоматически корректируется, чтобы
 *                 соответствовать ему.
 * @param pageNumber номер страницы. Принимает значение от 0 и выше. Если задано значение меньше 0,
 *                   то в итоге параметру будет присвоено значение 0.
 * @param totalItems общее кол-во элементов в выборке, по которой проводится пагинация.
 */
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
}
