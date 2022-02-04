package database.dao;

import database.DatabaseConnection;
import database.model.PersonEntity;
import database.model.StageEntity;

import javax.persistence.TypedQuery;
import java.util.List;

public class StageDao implements  DaoI<StageEntity> {

    DatabaseConnection connection = new DatabaseConnection();

    @Override
    public StageEntity get(long id) {
        return connection.getEntityManager().find(StageEntity.class, id);
    }

    @Override
    public List<StageEntity> getAll() {
        TypedQuery<StageEntity> query = connection.getEntityManager().createQuery("SELECT p FROM StageEntity p", StageEntity.class);
        return query.getResultList();
    }

    @Override
    public void create(StageEntity stageEntity) {
        connection.executeTransaction(entityManager -> entityManager.persist(stageEntity));
    }

    @Override
    public void update(StageEntity stageEntity) {
        connection.executeTransaction(entityManager -> entityManager.merge(stageEntity));
    }

    @Override
    public void delete(StageEntity stageEntity) {
        connection.executeTransaction(entityManager -> entityManager.remove(stageEntity));
    }
}
