/**
 * RPL-LICENSE NOTICE
 * <br><br>
 * This Sourcecode is under the RPL-LICENSE. <br>
 * License at: <a href="https://github.com/rubrionmc/.github/blob/main/licensens/RUBRION_PUBLIC">GITHUB</a>
 * <br><br>
 * Copyright (c) LeyCM <leycm@proton.me> <br>
 * Copyright (c) maintainers <br>
 * Copyright (c) contributors
 */
package net.rubrion.config.api.adapter;

/**
 * Registry for managing type adapters that handle conversion between different data types.
 * This registry allows registration and retrieval of type adapters for specific class types,
 * enabling flexible type conversion within the configuration system.
 *
 * @author LeyCM
 * @since 2.0.2
 */
public interface TypeAdapterRegistry {

    /**
     * Registers a type adapter for the specified class type.
     * If an adapter is already registered for the given type, it will be replaced.
     *
     * @param <T> the type that the adapter handles
     * @param type the class object representing the type to register the adapter for
     * @param adapter the type adapter instance to register
     * @throws IllegalArgumentException if either type or adapter is null
     *
     * @author LeyCM
     * @since 2.0.2
     */
    <T> void register(Class<T> type, TypeAdapter<T> adapter);

    /**
     * Retrieves the type adapter registered for the specified class type.
     *
     * @param <T> the type that the adapter handles
     * @param type the class object representing the type to get the adapter for
     * @return the registered type adapter for the specified type, or null if no adapter is registered
     * @throws IllegalArgumentException if type is null
     *
     * @author LeyCM
     * @since 2.0.2
     */
    <T> TypeAdapter<T> getAdapter(Class<T> type);

    /**
     * Checks whether an adapter is registered for the specified class type.
     *
     * @param type the class object representing the type to check
     * @return true if an adapter is registered for the specified type, false otherwise
     * @throws IllegalArgumentException if type is null
     *
     * @author LeyCM
     * @since 2.0.2
     */
    boolean hasAdapter(Class<?> type);

    /**
     * Converts a value to the specified target type using the appropriate type adapter.
     * This method will look up the registered adapter for the target type and use it
     * to perform the conversion.
     *
     * @param <T> the target type to convert to
     * @param value the value to be converted
     * @param targetType the class object representing the target type
     * @return the converted value of type T
     * @throws IllegalArgumentException if targetType is null or if no adapter is found for the target type
     * @throws ClassCastException if the value cannot be converted to the target type
     *
     * @author LeyCM
     * @since 2.0.2
     */
    <T> T convert(Object value, Class<T> targetType);
}