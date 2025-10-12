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
package net.rubrion.config.api.field;

/**
 * A reactive field that always reflects the current configuration value.
 * This interface provides type-safe access to configuration values with
 * automatic saving capabilities and default value fallbacks.
 *
 * @param <T> the type of the configuration value this field represents
 *
 * @author LeyCM
 * @since 2.0.2
 */
public interface Field<T> {

    /**
     * Gets the current value of this configuration field.
     * Returns the most recent value as stored in the configuration system.
     *
     * @return the current value of this field, may be null if no value is set
     *
     * @author LeyCM
     * @since 2.0.2
     */
    T get();

    /**
     * Sets the value of this configuration field and automatically persists
     * the change to the underlying configuration storage.
     *
     * @param value the new value to set for this field
     *
     * @author LeyCM
     * @since 2.0.2
     */
    void set(T value);

    /**
     * Gets the current value of this field, or returns the specified default value
     * if no value is currently set or the field doesn't exist.
     *
     * @param defaultValue the default value to return if no value is set
     * @return the current value if exists, otherwise the specified default value
     *
     * @author LeyCM
     * @since 2.0.2
     */
    T getOr(T defaultValue);

    /**
     * Checks whether this configuration field currently exists and has a value
     * set in the configuration system.
     *
     * @return true if this field exists and has a value, false otherwise
     *
     * @author LeyCM
     * @since 2.0.2
     */
    boolean exists();

    /**
     * Gets the configuration key that this field represents.
     * The key is the unique identifier used to store and retrieve this field
     * from the configuration system.
     *
     * @return the configuration key as a String
     *
     * @author LeyCM
     * @since 2.0.2
     */
    String key();
}