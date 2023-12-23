package com.bakuard.simpleCrud.dal;

import com.bakuard.simpleCrud.model.Student;

/**
 * Реализации этого интерфейса отвечают за сохранение объектов {@link Student} в постоянное
 * хранилище данных.
 */
public interface StudentRepository {

    /**
     * Сохраняет нового студента в постоянное хранилище.
     * @return этот же объект {@link Student}, но с установленным идентификатором.
     * @throws com.bakuard.simpleCrud.exception.DuplicateStudentException если в хранилище уже есть
     * пользователь с такой же комбинацией имени, фамилии и отчества.
     * @throws com.bakuard.simpleCrud.exception.UnknownGroupException если в хранилище нет группы
     * соответствующей {@link Student#getGroup()}
     */
    public Student add(Student newStudent);

    /**
     * Возвращает часть выборки из всех студентов. Искомая выборка сортируется по идентификаторам в порядке
     * возрастания.
     * @param pageNumber номер страницы.
     * @param pageSize размер страницы.
     * @return часть искомой выборки в виде объекта {@link Page}
     */
    public Page<Student> getAll(int pageNumber, int pageSize);

    /**
     * Возвращает студента по его идентификатору.
     * @throws com.bakuard.simpleCrud.exception.UnknownStudentException если в хранилище нет студента с таким
     * идентификатором.
     */
    public Student tryGetById(long id);

    /**
     * Удаляет студента по его идентификатору.
     * @throws com.bakuard.simpleCrud.exception.UnknownStudentException если в хранилище нет студента с таким
     * идентификатором.
     */
    public void tryDeleteById(long id);
}
