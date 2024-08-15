package org.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OptionalTypeAdapterTest {

    @Mock
    private TypeAdapter<String> typeAdapter;

    @Mock
    private JsonReader jsonReader;

    @Mock
    private JsonWriter jsonWriter;

    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testWriteWithValuePresent() throws IOException {
        Optional<String> optionalValue = Optional.of("testValue");
        OptionalTypeAdapter<String> adapter = new OptionalTypeAdapter<>(typeAdapter);

        doNothing().when(typeAdapter).write(any(JsonWriter.class), eq("testValue"));

        adapter.write(jsonWriter, optionalValue);

        verify(typeAdapter, times(1)).write(any(JsonWriter.class), eq("testValue"));
    }

    @Test
    public void testWriteWithEmptyValue() throws IOException {
        Optional<String> optionalValue = Optional.empty();
        OptionalTypeAdapter<String> adapter = new OptionalTypeAdapter<>(typeAdapter);

        adapter.write(jsonWriter, optionalValue);

        verify(jsonWriter, times(1)).nullValue();
        verify(typeAdapter, never()).write(any(JsonWriter.class), anyString());
    }

    @Test
    public void testReadWithNullToken() throws IOException {
        OptionalTypeAdapter<String> adapter = new OptionalTypeAdapter<>(typeAdapter);
        when(jsonReader.peek()).thenReturn(JsonToken.NULL);

        Optional<String> result = adapter.read(jsonReader);

        assertEquals(Optional.empty(), result);
        verify(jsonReader, times(1)).nextNull();
        verify(typeAdapter, never()).read(any(JsonReader.class));
    }

    @Test
    public void testReadWithValuePresent() throws IOException {
        OptionalTypeAdapter<String> adapter = new OptionalTypeAdapter<>(typeAdapter);
        when(typeAdapter.read(any(JsonReader.class))).thenReturn("testValue");

        Optional<String> result = adapter.read(jsonReader);

        assertEquals(Optional.of("testValue"), result);
        verify(typeAdapter, times(1)).read(any(JsonReader.class));
    }
}
