package database.dao;

import database.DatabaseConnection;
import database.model.ParticipationEntity;

import javax.persistence.TypedQuery;
import java.util.List;

public class ParticipationDao implements DaoI<ParticipationEntity> {

    DatabaseConnection connection = new DatabaseConnection();

    @Override
    public ParticipationEntity get(long id) {
        return connection.getEntityManager().find(ParticipationEntity.class, id);
    }

    @Override
    public List<ParticipationEntity> getAll() {
        TypedQuery<ParticipationEntity> query = connection.getEntityManager().createQuery("SELECT s FROM ParticipationEntity s", ParticipationEntity.class);
        return query.getResultList();
    }

    @Override
    public void create(ParticipationEntity participationEntity) {
        connection.executeTransaction(entityManager -> entityManager.persist(participationEntity));
    }

    public ParticipationEntity verifyPersonExist(long maxId, long idPerson) {
        TypedQuery<ParticipationEntity> query = connection.getEntityManager()
                .createQuery("SELECT p FROM ParticipationEntity p WHERE p.idPerson= :id and p.id=:maxId", ParticipationEntity.class)
                .setParameter("id", idPerson)
                .setParameter("maxId", maxId);
        return query.getSingleResult();
    }

    @Override
    public void update(ParticipationEntity participationEntity) {
        connection.executeTransaction(entityManager -> entityManager.merge(participationEntity));
    }

    @Override
    public void delete(ParticipationEntity participationEntity) {
        connection.executeTransaction(entityManager -> entityManager.remove(participationEntity));
    }


    public Long getScoreOfATeamAtAStage(long stageId, long teamId) {
        TypedQuery<Long> query = connection.getEntityManager()
                .createQuery("SELECT SUM(p.points) FROM TeamEntity t INNER JOIN PersonEntity c ON t.id = c.idTeam " +
                        "INNER JOIN ParticipationEntity p ON p.idPerson = c.id WHERE t.id = :teamId AND p.id = :stageId", Long.class)
                .setParameter("teamId",teamId)
                .setParameter("stageId",stageId);
        return query.getSingleResult();
    }


    public List<ParticipationEntity> getParticipationsAfterStage(long id) {
        TypedQuery<ParticipationEntity> query = connection.getEntityManager()
                .createQuery("SELECT p FROM ParticipationEntity p WHERE p.id = :id", ParticipationEntity.class)
                .setParameter("id", id);
        return query.getResultList();
    }
}
