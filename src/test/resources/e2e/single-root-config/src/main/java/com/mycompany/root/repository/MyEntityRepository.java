package com.mycompany.root.repository;

public interface MyEntityRepository {

    MyEntity findById(Long id);

    void insert(MyEntity entity);

    void update(MyEntity entity, Long id);

    void delete(Long id);
}
