package database.dao;

import java.util.List;

public interface DaoI<T> {
    T get(long id);
    List<T> getAll();
    void create(T t);
    void update(T T);
    void delete(T T);
}
