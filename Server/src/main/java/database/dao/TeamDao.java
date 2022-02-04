package database.dao;

import database.DatabaseConnection;
import database.model.PersonEntity;
import database.model.StageEntity;
import database.model.TeamEntity;

import javax.persistence.TypedQuery;
import java.util.List;

public class TeamDao implements DaoI<TeamEntity>{

    DatabaseConnection connection = new DatabaseConnection();

    @Override
    public TeamEntity get(long id) {
        return connection.getEntityManager().find(TeamEntity.class, id);
    }

    @Override
    public List<TeamEntity> getAll() {
        TypedQuery<TeamEntity> query = connection.getEntityManager().createQuery("SELECT p FROM TeamEntity p", TeamEntity.class);
        return query.getResultList();
    }

    @Override
    public void create(TeamEntity teamEntity) {
        connection.executeTransaction(entityManager -> entityManager.persist(teamEntity));
    }

    @Override
    public void update(TeamEntity teamEntity) {
        connection.executeTransaction(entityManager -> entityManager.merge(teamEntity));
    }

    @Override
    public void delete(TeamEntity teamEntity) {
        connection.executeTransaction(entityManager -> entityManager.remove(teamEntity));
    }

    public TeamEntity getTeamAfterName(String name) {
        TypedQuery<TeamEntity> query = connection.getEntityManager()
                .createQuery("SELECT t FROM TeamEntity t WHERE t.name= :name", TeamEntity.class)
                .setParameter("name", name);
        return query.getSingleResult();
    }
}
