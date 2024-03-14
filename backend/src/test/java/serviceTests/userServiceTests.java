package serviceTests;

import org.example.model.UserEntity;
import org.example.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class userServiceTests {
    private EntityManager entityManager;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        entityManager = mock(EntityManager.class);
        userService = new UserService(entityManager);
    }

    @AfterEach
    public void tearDown() {
        userService.close();
    }

    @Test
    public void findUserByEmailWhenUserExistsTest(){
        String email = "test@example.com";
        UserEntity expectedUser = new UserEntity();
        expectedUser.setEmail(email);

        Query query = mock(Query.class);
        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(expectedUser));

        UserEntity actualUser = userService.findUserByEmail(email);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    public void findUserByEmailReturnsNullWhenUserDoesNotExist(){
        String email = "nonexistent@example.com";

        Query query = mock(Query.class);

        when(entityManager.createQuery(anyString())).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of());

        UserEntity actualUser = userService.findUserByEmail(email);

        assertNull(actualUser);
    }

    @Test
    public void persistUserWorksCorrectly(){
        UserEntity user = new UserEntity();

        EntityTransaction transaction = mock(EntityTransaction.class);
        when(entityManager.getTransaction()).thenReturn(transaction);

        userService.persistUser(user);

        verify(transaction).begin();
        verify(entityManager).persist(user);
        verify(transaction).commit();
    }

    @Test
    public void deleteUserWorksCorrectly(){
        UserEntity user = new UserEntity();

        EntityTransaction transaction = mock(EntityTransaction.class);
        when(entityManager.getTransaction()).thenReturn(transaction);

        userService.deleteUser(user);

        verify(transaction).begin();
        verify(entityManager).remove(user);
        verify(transaction).commit();
    }
}
