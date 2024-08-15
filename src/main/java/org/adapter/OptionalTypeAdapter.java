package org.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Optional;

public class OptionalTypeAdapter<T> extends TypeAdapter<Optional<T>> {
    private final TypeAdapter<T> adapter;

    public OptionalTypeAdapter(TypeAdapter<T> adapter) {
        this.adapter = adapter;
    }

    @Override
    public void write(JsonWriter out, Optional<T> value) throws IOException {
        if (value.isPresent()) {
            adapter.write(out, value.get());
        } else {
            out.nullValue();
        }
    }

    @Override
    public Optional<T> read(JsonReader in) throws IOException {
        JsonToken peek = in.peek();
        if (peek == JsonToken.NULL) {
            in.nextNull();
            return Optional.empty();
        } else {
            T val = adapter.read(in);
            return Optional.ofNullable(val);
        }
    }
}