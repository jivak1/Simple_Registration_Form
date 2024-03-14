package modelTests;

import org.example.model.BaseEntity;
import org.junit.jupiter.api.Test;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BaseEntityTests {
    @Test
    void baseEntityAnnotationsTests() {
        Class<BaseEntity> baseEntityClass = BaseEntity.class;

        MappedSuperclass mappedSuperclassAnnotation = baseEntityClass.getAnnotation(MappedSuperclass.class);

        assertEquals(MappedSuperclass.class, mappedSuperclassAnnotation.annotationType(), "@MappedSuperclass annotation is missing");

        try {
            Field idField = baseEntityClass.getDeclaredField("id");
            Id idAnnotation = idField.getAnnotation(Id.class);
            GeneratedValue generatedValueAnnotation = idField.getAnnotation(GeneratedValue.class);

            assertEquals(Id.class, idAnnotation.annotationType(), "@Id annotation is missing on id field");

            assertEquals(GeneratedValue.class, generatedValueAnnotation.annotationType(), "@GeneratedValue annotation is missing on id field");

            assertEquals(GenerationType.IDENTITY, generatedValueAnnotation.strategy(), "Incorrect GenerationType for id field");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            assertEquals(true, false, "id field not found in BaseEntity");
        }
    }
}
