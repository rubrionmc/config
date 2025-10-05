package net.rubrion.config.api.adapter;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class Adapters {

    private static final Map<Class<?>, TypeAdapter<?>> adapters = new ConcurrentHashMap<>();

    static {
        register(new StringAdapter());
        register(new IntegerAdapter());
        register(new LongAdapter());
        register(new DoubleAdapter());
        register(new BooleanAdapter());
        register(new UUIDAdapter());
    }

    private Adapters() {}

    public static <T> void register(TypeAdapter<T> adapter) {
        adapters.put(adapter.getType(), adapter);
    }

    @SuppressWarnings("unchecked")
    public static <T> TypeAdapter<T> get(Class<T> type) {
        return (TypeAdapter<T>) adapters.get(type);
    }

    public static boolean has(Class<?> type) {
        return adapters.containsKey(type);
    }

    private static class StringAdapter implements TypeAdapter<String> {
        public Object serialize(String value) { return value; }
        public String deserialize(@NotNull Object raw, Class<String> type) { return raw.toString(); }
        public Class<String> getType() { return String.class; }
    }

    private static class IntegerAdapter implements TypeAdapter<Integer> {
        public Object serialize(Integer value) { return value; }
        public Integer deserialize(Object raw, Class<Integer> type) {
            if (raw instanceof Number) return ((Number) raw).intValue();
            return Integer.parseInt(raw.toString());
        }
        public Class<Integer> getType() { return Integer.class; }
    }

    private static class LongAdapter implements TypeAdapter<Long> {
        public Object serialize(Long value) { return value; }
        public Long deserialize(Object raw, Class<Long> type) {
            if (raw instanceof Number) return ((Number) raw).longValue();
            return Long.parseLong(raw.toString());
        }
        public Class<Long> getType() { return Long.class; }
    }

    private static class DoubleAdapter implements TypeAdapter<Double> {
        public Object serialize(Double value) { return value; }
        public Double deserialize(Object raw, Class<Double> type) {
            if (raw instanceof Number) return ((Number) raw).doubleValue();
            return Double.parseDouble(raw.toString());
        }
        public Class<Double> getType() { return Double.class; }
    }

    private static class BooleanAdapter implements TypeAdapter<Boolean> {
        public Object serialize(Boolean value) { return value; }
        public @NotNull Boolean deserialize(Object raw, Class<Boolean> type) {
            if (raw instanceof Boolean) return (Boolean) raw;
            return Boolean.parseBoolean(raw.toString());
        }
        public Class<Boolean> getType() { return Boolean.class; }
    }

    private static class UUIDAdapter implements TypeAdapter<UUID> {
        @Contract(pure = true)
        public @Unmodifiable Object serialize(@NotNull UUID value) { return value.toString(); }
        public @NotNull UUID deserialize(@NotNull Object raw, Class<UUID> type) {
            return UUID.fromString(raw.toString());
        }
        public Class<UUID> getType() { return UUID.class; }
    }
}