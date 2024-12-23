package io.javaside.jharmonizer.kaligosample.merge;

import lombok.Getter;

import java.util.List;

public class FieldDef<T> {

    @Getter
    private final String name;

    private final Class<T> valueType;

    private FieldDef(String name, Class<T> valueType) {
        this.name = name;
        this.valueType = valueType;
    }

    private FieldDef(String name, boolean isCollection, Class<?> valueType) {
        this.name = name;
        this.valueType = (Class<T>) valueType;
    }

    public static <T> FieldDef<T> of(String name, Class<T> valueType) {
        return new FieldDef<>(name, valueType);
    }

    public static<T> FieldDef<List<T>> ofList(String name, Class<T> valueType) {
        return new FieldDef<>(name, true, List.class);
    }

    public T parseValue(Object value) {
        return valueType.cast(value);
    }
}
