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

import java.io.IOException;
import java.util.Map;

/**
 * Adapter interface for handling different configuration file formats.
 * Implementations of this interface provide read/write capabilities for
 * various configuration formats while preserving structure and comments.
 *
 * @author LeyCM
 * @since 2.0.2
 */
public interface ConfigAdapter {

    /**
     * Reads configuration data from a string content and parses it into a nested Map structure.
     * The method processes the configuration content and converts it into a hierarchical
     * key-value representation where nested sections are represented as nested Maps.
     *
     * @param content The configuration file content as a string
     * @return Parsed configuration as a nested Map structure where keys represent
     *         configuration paths and values represent configuration values
     * @throws IOException if the content cannot be parsed due to format violations
     *         or malformed configuration data
     * @throws IllegalArgumentException if the content is null or empty
     *
     * @author LeyCM
     * @since 2.0.2
     */
    Map<String, Object> read(String content) throws IOException;

    /**
     * Writes configuration data to string format while preserving the original structure and comments.
     * This method takes the current file content and new data, merging them to maintain
     * existing formatting, comments, and organizational structure.
     *
     * @param current The current file content used as a base for preserving comments and structure
     * @param data The configuration data to write, organized as a nested Map structure
     * @return Formatted string representation of the configuration data
     * @throws IOException if the data cannot be written due to format constraints
     *         or invalid data structure
     * @throws IllegalArgumentException if the data Map is null or contains invalid types
     *
     * @author LeyCM
     * @since 2.0.2
     */
    String write(String current, Map<String, Object> data) throws IOException;

    /**
     * Updates a single value in the configuration while preserving all other content,
     * including comments, formatting, and unrelated configuration values.
     * The key should be provided in dot notation for nested configuration paths.
     *
     * @param current The current file content to be updated
     * @param key The configuration key to update, using dot notation for nested paths
     *            (e.g., "database.connection.timeout")
     * @param value The new value to set for the specified key
     * @return Updated configuration content with the specified value changed
     * @throws IOException if the update operation fails due to format issues
     *         or if the key cannot be located in the configuration
     * @throws IllegalArgumentException if the key is null, empty, or malformed,
     *         or if the value type is not supported by the configuration format
     *
     * @author LeyCM
     * @since 2.0.2
     */
    String updateValue(String current, String key, Object value) throws IOException;

    /**
     * Gets the file extensions supported by this configuration adapter.
     * The extensions should be returned in lowercase without the leading dot.
     *
     * @return Array of supported file extensions (e.g., ["json", "yaml", "yml", "conf"])
     *
     * @author LeyCM
     * @since 2.0.2
     */
    String[] getSupportedExtensions();
}