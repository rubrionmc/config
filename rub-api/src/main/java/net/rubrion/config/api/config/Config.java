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

import net.rubrion.config.api.ConfigApiProvider;
import net.rubrion.config.api.exception.ConfigReadException;
import net.rubrion.config.api.field.Field;
import net.rubrion.config.api.field.FieldList;
import net.rubrion.config.api.field.FieldSection;

import java.nio.file.Path;
import java.util.Optional;

/**
 * Main configuration interface providing type-safe access to configuration values.
 * This interface serves as the primary entry point for reading, writing, and managing
 * configuration data with support for reactive field bindings and automatic persistence.
 *
 * @author LeyCM
 * @since 2.0.2
 */
public interface Config {

    /**
     * Reads a configuration file from the default configuration directory.
     *
     * @param filename the name of the configuration file to read
     * @return a Config instance containing the parsed configuration data
     * @throws ConfigReadException if the file cannot be read or parsed
     *
     * @author LeyCM
     * @since 2.0.2
     */
    static Config read(String filename) {
        return ConfigApiProvider.get().read(filename);
    }

    /**
     * Reads a configuration file from the specified file path.
     *
     * @param path the full path to the configuration file
     * @return a Config instance containing the parsed configuration data
     * @throws IllegalArgumentException if the path is null or invalid
     * @throws ConfigReadException if the file cannot be read or parsed
     *
     * @author LeyCM
     * @since 2.0.2
     */
    static Config read(Path path) {
        return ConfigApiProvider.get().read(path);
    }

    /**
     * Retrieves a configuration value by key with type safety.
     * Supports dot notation for nested keys (e.g., "database.host").
     *
     * @param <T> the type of the configuration value
     * @param key the configuration key (supports dot notation like "database.host")
     * @param type the expected class type of the value
     * @return an Optional containing the value if present and of correct type, empty otherwise
     * @throws IllegalArgumentException if key is null or empty, or if type is null
     *
     * @author LeyCM
     * @since 2.0.2
     */
    <T> Optional<T> get(String key, Class<T> type);

    /**
     * Retrieves a configuration value or returns a default value if not present.
     *
     * @param <T> the type of the configuration value
     * @param key the configuration key
     * @param type the expected class type of the value
     * @param defaultValue the default value to return if the key doesn't exist or is of wrong type
     * @return the configuration value if present and of correct type, otherwise the defaultValue
     * @throws IllegalArgumentException if key is null or empty, type is null, or defaultValue is null
     *
     * @author LeyCM
     * @since 2.0.2
     */
    <T> T getOr(String key, Class<T> type, T defaultValue);

    /**
     * Gets a reactive field that always reflects the current configuration value.
     * Changes to the field will be automatically persisted to the configuration.
     *
     * @param <T> the type of the field value
     * @param key the configuration key
     * @param type the expected class type of the field value
     * @return a Field instance that can get/set the value reactively
     * @throws IllegalArgumentException if key is null or empty, or if type is null
     *
     * @author LeyCM
     * @since 2.0.2
     */
    <T> Field<T> getField(String key, Class<T> type);

    /**
     * Gets a reactive field for a list of configuration values.
     *
     * @param <T> the type of list elements
     * @param key the configuration key for the list
     * @param elementType the expected class type of list elements
     * @return a FieldList instance for managing the list reactively
     * @throws IllegalArgumentException if key is null or empty, or if elementType is null
     *
     * @author LeyCM
     * @since 2.0.2
     */
    <T> FieldList<T> getFieldList(String key, Class<T> elementType);

    /**
     * Gets a reactive field for a configuration section (nested configuration).
     *
     * @param key the configuration key for the section
     * @return a FieldSection instance for managing the configuration section reactively
     * @throws IllegalArgumentException if key is null or empty
     *
     * @author LeyCM
     * @since 2.0.2
     */
    FieldSection getFieldSection(String key);

    /**
     * Sets a value in the configuration and automatically saves changes to disk.
     *
     * @param key the configuration key
     * @param value the value to set (must be serializable)
     * @throws IllegalArgumentException if key is null or empty, or if value is null
     *
     * @author LeyCM
     * @since 2.0.2
     */
    void set(String key, Object value);

    /**
     * Reloads the configuration from disk, discarding any unsaved changes.
     *
     * @author LeyCM
     * @since 2.0.2
     */
    void reload();

    /**
     * Manually saves the current configuration state to disk.
     *
     * @author LeyCM
     * @since 2.0.2
     */
    void save();

    /**
     * Gets the file path where this configuration is stored.
     *
     * @return the Path to the configuration file
     *
     * @author LeyCM
     * @since 2.0.2
     */
    Path getPath();
}