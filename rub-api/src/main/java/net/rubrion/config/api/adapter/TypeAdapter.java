package net.rubrion.config.api.adapter;

/**
 * Type adapter for converting between config values and Java objects
 */
public interface TypeAdapter<T> {

    /**
     * Converts from config representation to Java object
     */
    T fromConfig(Object configValue);

    /**
     * Converts from Java object to config representation
     */
    Object toConfig(T value);

    /**
     * Gets the Java type this adapter handles
     */
    Class<T> getType();
}
