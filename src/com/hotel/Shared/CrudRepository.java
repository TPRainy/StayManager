package com.hotel.Shared;

import java.util.List;

public interface CrudRepository<T> {
    T getById(int id);
    List<T> getAll();
    T save(T entity);
    void delete(int id);
    void update(T entity);
}