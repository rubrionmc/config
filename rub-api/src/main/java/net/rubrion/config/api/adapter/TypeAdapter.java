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
 * A type adapter for converting between configuration values and Java objects.
 * This interface defines the contract for serializing and deserializing objects
 * to and from configuration storage formats.
 *
 * @param <T> the type of Java object this adapter handles
 *
 * @author LeyCM
 * @since 2.0.2
 */
public interface TypeAdapter<T> {

    /**
     * Converts a configuration representation to a Java object.
     * This method deserializes a configuration value (typically from a config file)
     * into its corresponding Java object representation.
     *
     * @param configValue the configuration value to convert, typically a primitive
     *                   wrapper, String, Map, or List depending on the configuration format
     * @return the converted Java object of type T
     * @throws ClassCastException if the configValue cannot be converted to type T
     * @throws IllegalArgumentException if the configValue is in an invalid format
     *
     * @author LeyCM
     * @since 2.0.2
     */
    T fromConfig(Object configValue);

    /**
     * Converts a Java object to its configuration representation.
     * This method serializes a Java object into a format suitable for storage
     * in configuration files (typically primitives, Strings, Maps, or Lists).
     *
     * @param value the Java object to convert to configuration representation
     * @return the configuration representation of the value, typically a primitive
     *         wrapper, String, Map, or List
     * @throws IllegalArgumentException if the value cannot be converted to a valid
     *                                  configuration representation
     * @author LeyCM
     * @since 2.0.2
     */
    Object toConfig(T value);

    /**
     * Gets the Java type that this adapter handles.
     * This method returns the Class object representing the type parameter T,
     * which defines what type of objects this adapter can convert.
     *
     * @return the Class object representing the type T that this adapter handles
     *
     * @author LeyCM
     * @since 2.0.2
     */
    Class<T> getType();
}