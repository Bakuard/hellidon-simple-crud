package com.bakuard.simpleCrud.dal;

import com.bakuard.simpleCrud.model.Group;

/**
 * Реализации этого интерфейса отвечают за сохранение объектов {@link Group} в постоянное
 * хранилище данных.
 */
public interface GroupRepository {

    /**
     * Сохраняет новую группу в постоянном хранилище.
     * @return этот же объект, но с установленным идентификатором.
     * @throws com.bakuard.simpleCrud.exception.DuplicateGroupException если среди сохраненных групп уже
     * есть группа с таким именем или идентификатором.
     */
    public Group add(Group newGroup);

    /**
     * Возвращает часть выборки из всех групп. Искомая выборка сортируется по идентификаторам в порядке
     * возрастания.
     * @param pageNumber номер страницы.
     * @param pageSize размер страницы.
     * @return часть искомой выборки в виде объекта {@link Page}
     */
    public Page<Group> getAll(int pageNumber, int pageSize);

    /**
     * Возвращает группу по её идентификатору.
     * @throws com.bakuard.simpleCrud.exception.UnknownGroupException если в хранилище нет группы с таким
     * идентификатором.
     */
    public Group tryGetById(long id);
}
