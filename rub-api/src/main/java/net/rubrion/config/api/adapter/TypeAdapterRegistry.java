package net.rubrion.config.api.adapter;

/**
 * Registry for type adapters
 */
public interface TypeAdapterRegistry {

    /**
     * Registers a type adapter
     */
    <T> void register(Class<T> type, TypeAdapter<T> adapter);

    /**
     * Gets a type adapter for a type
     */
    <T> TypeAdapter<T> getAdapter(Class<T> type);

    /**
     * Checks if an adapter exists for a type
     */
    boolean hasAdapter(Class<?> type);

    /**
     * Converts a value using the appropriate adapter
     */
    <T> T convert(Object value, Class<T> targetType);
}
