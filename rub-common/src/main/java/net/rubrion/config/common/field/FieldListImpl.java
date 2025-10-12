package net.rubrion.config.common.field;

import net.rubrion.config.api.field.FieldList;
import net.rubrion.config.common.config.ConfigImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record FieldListImpl<T>(ConfigImpl config, String key,
                               Class<T> elementType) implements FieldList<T> {

    @Override
    @SuppressWarnings("unchecked")
    public List<T> get() {
        return config.get(key, List.class).orElse(Collections.emptyList());
    }

    @Override
    public void set(List<T> value) {
        config.set(key, value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> getOr(List<T> defaultValue) {
        return config.getOr(key, List.class, defaultValue);
    }

    @Override
    public boolean exists() {
        return config.get(key, List.class).isPresent();
    }


    @Override
    public void add(T item) {
        List<T> list = new ArrayList<>(get());
        list.add(item);
        set(list);
    }

    @Override
    public void remove(T item) {
        List<T> list = new ArrayList<>(get());
        list.remove(item);
        set(list);
    }

    @Override
    public int size() {
        return get().size();
    }

}
