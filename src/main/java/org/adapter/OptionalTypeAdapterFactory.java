package org.adapter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

public class OptionalTypeAdapterFactory implements TypeAdapterFactory {

    private static final OptionalTypeAdapterFactory INSTANCE = new OptionalTypeAdapterFactory();

    public static OptionalTypeAdapterFactory getInstance() {
        return INSTANCE;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Type type = typeToken.getType();

        if (typeToken.getRawType() != Optional.class) {
            return null; // This factory only handles Optional types
        }

        if (!(type instanceof ParameterizedType)) {
            return null; // Raw Optional, not handled
        }

        Type valueType = ((ParameterizedType) type).getActualTypeArguments()[0];
        TypeAdapter<?> valueAdapter = gson.getAdapter(TypeToken.get(valueType));

        return (TypeAdapter<T>) new OptionalTypeAdapter<>(valueAdapter);
    }
}