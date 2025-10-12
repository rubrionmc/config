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
import net.rubrion.config.api.field.Field;
import net.rubrion.config.api.field.FieldList;
import net.rubrion.config.api.field.FieldSection;

import java.nio.file.Path;
import java.util.Optional;

/**
 * Main configuration interface providing type-safe access to configuration values
 */
public interface Config {

    static Config read(String filename) {
        return ConfigApiProvider.get().read(filename);
    }

    /**
     * Reads a config with explicit path
     */
    static Config read(Path path) {
        return ConfigApiProvider.get().read(path);
    }

    /**
     * Gets a value from the config
     * @param key The configuration key (supports dot notation like "database.host")
     * @param type The expected type
     * @return Optional containing the value if present and of correct type
     */
    <T> Optional<T> get(String key, Class<T> type);

    /**
     * Gets a value or returns default
     * @param key The configuration key
     * @param type The expected type
     * @param defaultValue Default value if key doesn't exist
     * @return The value or default
     */
    <T> T getOr(String key, Class<T> type, T defaultValue);

    /**
     * Gets a reactive field that always reflects current config value
     * @param key The configuration key
     * @param type The expected type
     * @return A Field that can get/set the value
     */
    <T> Field<T> getField(String key, Class<T> type);

    /**
     * Gets a reactive field for a list
     */
    <T> FieldList<T> getFieldList(String key, Class<T> elementType);

    /**
     * Gets a reactive field for a configuration section
     */
    FieldSection getFieldSection(String key);

    /**
     * Sets a value in the config and auto-saves
     * @param key The configuration key
     * @param value The value to set
     */
    void set(String key, Object value);

    /**
     * Reloads the configuration from disk
     */
    void reload();

    /**
     * Manually saves the configuration
     */
    void save();

    /**
     * Gets the file path of this config
     */
    Path getPath();
}