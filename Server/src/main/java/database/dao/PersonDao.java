package database.dao;

import database.DatabaseConnection;
import database.model.ParticipationEntity;
import database.model.PersonEntity;
import database.model.TeamEntity;

import javax.persistence.TypedQuery;
import java.util.List;

public class PersonDao implements DaoI<PersonEntity>{

    DatabaseConnection connection = new DatabaseConnection();

    @Override
    public PersonEntity get(long id) {
        return connection.getEntityManager().find(PersonEntity.class, id);
    }

    @Override
    public List<PersonEntity> getAll() {
        TypedQuery<PersonEntity> query = connection.getEntityManager().createQuery("SELECT p FROM PersonEntity p", PersonEntity.class);
        return query.getResultList();
    }

    @Override
    public void create(PersonEntity personEntity) {
        connection.executeTransaction(entityManager -> entityManager.persist(personEntity));
    }

    @Override
    public void update(PersonEntity personEntity) {
        connection.executeTransaction(entityManager -> entityManager.merge(personEntity));
    }

    @Override
    public void delete(PersonEntity personEntity) {
        connection.executeTransaction(entityManager -> entityManager.remove(personEntity));
    }

    public PersonEntity getType(String username) {
        TypedQuery<PersonEntity> query = connection.getEntityManager()
                .createQuery("SELECT p FROM PersonEntity p WHERE p.username= :username", PersonEntity.class)
                .setParameter("username", username);
        return query.getSingleResult();
    }
}
