package database;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.function.Consumer;

public class DatabaseConnection {
    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    public DatabaseConnection() {
        this.initTransaction();
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public boolean executeTransaction(Consumer<EntityManager> action) {
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();
            action.accept(entityManager);
            entityTransaction.commit();

        } catch(RuntimeException e) {
            System.err.println("Transaction Error" + e.getMessage());
            entityTransaction.rollback();
            return false;
        }
        return true;
    }

    public void initTransaction() {
        try {
            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("competition");
            entityManager = entityManagerFactory.createEntityManager();
        } catch(Exception e) {
            System.err.println("Error at initializing" + e.getMessage());
        }

    }
}
