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
        //clears entity manager, because it cashes query results which leads to issues with changing user data after some time
        entityManager.clear();
        //query with JPQL, that finds users with that email(there can be only one in our case)
        String findByEmailQuery = "SELECT u FROM UserEntity u WHERE u.email = :email" ;
        //create query
        Query query = entityManager.createQuery(findByEmailQuery) ;
        //sets parameter
        query.setParameter("email", email) ;
        //get result
        List<UserEntity> users = query.getResultList() ;
        //if there is no user with that email found return null
        if(users.isEmpty()){
            return null ;
        }else {
            //return user
            return users.getFirst() ;
        }
    }
    //persists user to the db
    public void persistUser(UserEntity user){
        entityManager.getTransaction().begin();

        entityManager.persist(user);

        entityManager.getTransaction().commit();

    }
    //deletes user TODO user deletion endpoint + logic (button inside the edit user for with delete user that calls to delete-user backend endpoint)
    public void deleteUser(UserEntity user){
        entityManager.getTransaction().begin();

        entityManager.remove(user);

        entityManager.getTransaction().commit();

    }

    public UserService(EntityManager entityManager){
        this.entityManager = entityManager ;

    }
    public void close(){
        if (entityManager != null) {
            entityManager.close();
        }
    }
}
