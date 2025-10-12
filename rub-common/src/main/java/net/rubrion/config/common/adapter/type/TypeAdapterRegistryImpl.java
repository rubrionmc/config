package net.rubrion.config.common.adapter.type;


import net.rubrion.config.api.adapter.TypeAdapter;
import net.rubrion.config.api.adapter.TypeAdapterRegistry;

import java.util.*;

public class TypeAdapterRegistryImpl implements TypeAdapterRegistry {
    private final Map<Class<?>, TypeAdapter<?>> adapters;

    public TypeAdapterRegistryImpl() {
        this.adapters = new HashMap<>();
        registerDefaultAdapters();
    }

    private void registerDefaultAdapters() {
        register(String.class, new StringAdapter());
        register(Integer.class, new IntegerAdapter());
        register(int.class, new IntegerAdapter());
        register(Long.class, new LongAdapter());
        register(long.class, new LongAdapter());
        register(Double.class, new DoubleAdapter());
        register(double.class, new DoubleAdapter());
        register(Float.class, new FloatAdapter());
        register(float.class, new FloatAdapter());
        register(Boolean.class, new BooleanAdapter());
        register(boolean.class, new BooleanAdapter());

        register(List.class, new ListAdapter());
        register(Map.class, new MapAdapter());
    }

    @Override
    public <T> void register(Class<T> type, TypeAdapter<T> adapter) {
        adapters.put(type, adapter);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> getAdapter(Class<T> type) {
        TypeAdapter<?> adapter = adapters.get(type);
        if (adapter == null) {
            for (Map.Entry<Class<?>, TypeAdapter<?>> entry : adapters.entrySet()) {
                if (entry.getKey().isAssignableFrom(type)) {
                    return (TypeAdapter<T>) entry.getValue();
                }
            }
        }
        return (TypeAdapter<T>) adapter;
    }

    @Override
    public boolean hasAdapter(Class<?> type) {
        return getAdapter((Class<?>) type) != null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T convert(Object value, Class<T> targetType) {
        if (value == null) {
            return null;
        }

        if (targetType.isInstance(value)) {
            return (T) value;
        }

        TypeAdapter<T> adapter = getAdapter(targetType);
        if (adapter != null) {
            return adapter.fromConfig(value);
        }

        throw new IllegalArgumentException("No adapter found for type: " + targetType.getName());
    }
}
