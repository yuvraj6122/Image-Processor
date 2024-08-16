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
        OptionalTypeAdapterFactory factory = OptionalTypeAdapterFactory.getInstance();
        TypeToken<Optional<String>> typeToken = new TypeToken<Optional<String>>() {};
        when(mockGson.getAdapter(TypeToken.get(String.class))).thenReturn((TypeAdapter<String>) mockValueAdapter);
        TypeAdapter<Optional<String>> adapter = factory.create(mockGson, typeToken);
        assertNotNull(adapter);
        assertTrue(adapter instanceof OptionalTypeAdapter);
    }

    @Test
    public void testCreateWithRawOptionalType() {
        OptionalTypeAdapterFactory factory = OptionalTypeAdapterFactory.getInstance();
        TypeToken<Optional> typeToken = TypeToken.get(Optional.class);
        TypeAdapter<Optional> adapter = factory.create(mockGson, typeToken);
        assertNull(adapter);
    }

    @Test
    public void testCreateWithNonOptionalType() {
        OptionalTypeAdapterFactory factory = OptionalTypeAdapterFactory.getInstance();
        TypeToken<String> typeToken = TypeToken.get(String.class);
        TypeAdapter<String> adapter = factory.create(mockGson, typeToken);
        assertNull(adapter);
    }
}
