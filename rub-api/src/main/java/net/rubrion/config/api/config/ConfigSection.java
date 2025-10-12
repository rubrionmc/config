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
package net.rubrion.config.api.config;

import java.util.List;
import java.util.Optional;

/**
 * Represents a configuration section that can contain nested configuration values.
 * A configuration section acts as a hierarchical container for configuration data,
 * allowing organization of settings in a tree-like structure.
 *
 * @author LeyCM
 * @since 2.0.2
 */
public interface ConfigSection {

    /**
     * Retrieves a value from the configuration section by key and type.
     * Returns an empty Optional if the key doesn't exist or the value cannot be cast to the specified type.
     *
     * @param <T> the type of the value to retrieve
     * @param key the key to look up in the configuration section
     * @param type the class type of the value to retrieve
     * @return an Optional containing the value if found and type-compatible, empty Optional otherwise
     * @throws IllegalArgumentException if key is null or empty, or if type is null
     *
     * @author LeyCM
     * @since 2.0.2
     */
    <T> Optional<T> get(String key, Class<T> type);

    /**
     * Retrieves a value from the configuration section by key and type, returning a default value if not found.
     * This method provides a safe way to access configuration values with fallback behavior.
     *
     * @param <T> the type of the value to retrieve
     * @param key the key to look up in the configuration section
     * @param type the class type of the value to retrieve
     * @param defaultValue the default value to return if the key doesn't exist or type conversion fails
     * @return the configuration value if found and type-compatible, otherwise the defaultValue
     * @throws IllegalArgumentException if key is null or empty, or if type is null
     *
     * @author LeyCM
     * @since 2.0.2
     */
    <T> T getOr(String key, Class<T> type, T defaultValue);

    /**
     * Sets a value in the configuration section at the specified key.
     * If the key already exists, the value will be overwritten.
     * Supports nested keys using dot notation (e.g., "database.host").
     *
     * @param key the key where the value should be stored
     * @param value the value to store in the configuration section
     * @throws IllegalArgumentException if key is null or empty
     * @throws UnsupportedOperationException if the configuration is read-only
     *
     * @author LeyCM
     * @since 2.0.2
     */
    void set(String key, Object value);

    /**
     * Returns a list of all keys present in this configuration section.
     * The list includes only direct child keys of this section, not nested keys from subsections.
     * The returned list is typically unmodifiable.
     *
     * @return a List containing all keys in this configuration section, never null
     *
     * @author LeyCM
     * @since 2.0.2
     */
    List<String> getKeys();
}