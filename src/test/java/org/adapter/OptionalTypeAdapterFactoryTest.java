package org.adapter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OptionalTypeAdapterFactoryTest {

    @Mock
    private Gson mockGson;

    @Mock
    private TypeAdapter<?> mockValueAdapter;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateWithParameterizedType() {
        // Arrange
        OptionalTypeAdapterFactory factory = OptionalTypeAdapterFactory.getInstance();
        TypeToken<Optional<String>> typeToken = new TypeToken<Optional<String>>() {};
        when(mockGson.getAdapter(TypeToken.get(String.class))).thenReturn((TypeAdapter<String>) mockValueAdapter);

        // Act
        TypeAdapter<Optional<String>> adapter = factory.create(mockGson, typeToken);

        // Assert
        assertNotNull(adapter);
        assertTrue(adapter instanceof OptionalTypeAdapter);
    }

    @Test
    public void testCreateWithRawOptionalType() {
        // Arrange
        OptionalTypeAdapterFactory factory = OptionalTypeAdapterFactory.getInstance();
        TypeToken<Optional> typeToken = TypeToken.get(Optional.class);

        // Act
        TypeAdapter<Optional> adapter = factory.create(mockGson, typeToken);

        // Assert
        assertNull(adapter); // Raw Optional types are not handled
    }

    @Test
    public void testCreateWithNonOptionalType() {
        // Arrange
        OptionalTypeAdapterFactory factory = OptionalTypeAdapterFactory.getInstance();
        TypeToken<String> typeToken = TypeToken.get(String.class);

        // Act
        TypeAdapter<String> adapter = factory.create(mockGson, typeToken);

        // Assert
        assertNull(adapter); // Non-Optional types are not handled
    }
}
