package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.model.UserEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

public class UserService {
    private final EntityManager entityManager ;

    public UserEntity findUserByEmail(String email){
        entityManager.clear();

        String findByEmailQuery = "SELECT u FROM UserEntity u WHERE u.email = :email" ;

        Query query = entityManager.createQuery(findByEmailQuery) ;

        query.setParameter("email", email) ;

        List<UserEntity> users = query.getResultList() ;

        if(users.isEmpty()){
            return null ;
        }else {
            return users.getFirst() ;
        }
    }
    public void persistUser(UserEntity user){
        entityManager.getTransaction().begin();

        entityManager.persist(user);

        entityManager.getTransaction().commit();

    }

    public void deleteUser(UserEntity user){
        entityManager.getTransaction().begin();

        entityManager.remove(user);

        entityManager.getTransaction().commit();

    }

    public UserService(EntityManager entityManager){
//        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("MainPersistenceUnit");
//
//        this.entityManager = entityManagerFactory.createEntityManager(); ;
        this.entityManager = entityManager ;

    }
    public void close(){
        if (entityManager != null) {
            entityManager.close();
        }
    }
}
