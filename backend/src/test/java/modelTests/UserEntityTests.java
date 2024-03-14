package modelTests;

import org.example.model.UserEntity;
import org.junit.jupiter.api.Test;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserEntityTests {
    @Test
    void userEntityAnnotationsTests() {
        Class<UserEntity> userEntityClass = UserEntity.class;

        Entity entityAnnotation = userEntityClass.getAnnotation(Entity.class);

        assertNotNull(entityAnnotation, "@Entity annotation is missing");
        assertEquals("users", ((Table) userEntityClass.getAnnotation(Table.class)).name(), "Incorrect table name");

        try {
            Field usernameField = userEntityClass.getDeclaredField("username");
            Field passwordField = userEntityClass.getDeclaredField("password");
            Field emailField = userEntityClass.getDeclaredField("email");
            Field isActiveField = userEntityClass.getDeclaredField("isActive");
            Field verificationTokenField = userEntityClass.getDeclaredField("verificationToken");
            Field sessionVerificationTokenField = userEntityClass.getDeclaredField("sessionVerificationToken");

            assertFieldAnnotation(usernameField, Column.class, "username", false, false);
            assertFieldAnnotation(passwordField, Column.class, "password", false, false);
            assertFieldAnnotation(emailField, Column.class, "email", false, true);
            assertFieldAnnotation(isActiveField, Column.class, "isActive", false, false);
            assertFieldAnnotation(verificationTokenField, Column.class, "verificationToken", false, false);
            assertFieldAnnotation(sessionVerificationTokenField, Column.class, "sessionVerificationToken", true, true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            assertEquals(true, false, "One or more fields not found in UserEntity");
        }
    }

    private void assertFieldAnnotation(Field field, Class<? extends Annotation> annotationClass, String fieldName, boolean isNullable, boolean isUnique) {
        assertNotNull(field.getAnnotation(annotationClass), "@" + annotationClass.getSimpleName() + " annotation is missing on " + fieldName + " field");
        Column columnAnnotation = field.getAnnotation(Column.class);
        assertNotNull(columnAnnotation, "@Column annotation is missing on " + fieldName + " field");
        assertEquals(isNullable, columnAnnotation.nullable(), "Incorrect nullable attribute for " + fieldName + " field");
        assertEquals(isUnique, columnAnnotation.unique(), "Incorrect unique attribute for " + fieldName + " field");
    }
}
