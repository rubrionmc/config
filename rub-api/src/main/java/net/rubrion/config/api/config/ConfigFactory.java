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

import java.nio.file.Path;

/**
 * Factory for creating and reading configurations from various file formats.
 * This interface provides methods to manage configuration files with automatic
 * format detection and fallback creation mechanisms.
 *
 * @author LeyCM
 * @since 2.0.2
 */
public interface ConfigFactory {

    /**
     * Reads a configuration file from the config directory. If the file doesn't exist:
     * <ol>
     *   <li>Attempts to copy from resources</li>
     *   <li>If not found in resources, creates an empty file</li>
     * </ol>
     * <p>
     * The file format is automatically detected based on the file extension:
     * <ul>
     *   <li>.json - JSON format</li>
     *   <li>.yml or .yaml - YAML format</li>
     *   <li>.toml - TOML format</li>
     * </ul>
     *
     * @param filename The filename relative to the config directory (e.g., "database.json")
     * @return The loaded configuration instance
     * @throws IllegalArgumentException if the filename is null, empty, or has an unsupported extension
     * @throws SecurityException if file access is denied by the security manager
     *
     * @author LeyCM
     * @since 2.0.2
     */
    Config read(String filename);

    /**
     * Reads a configuration file from an explicit file path.
     * <p>
     * The file format is automatically detected based on the file extension.
     * If the file doesn't exist, behavior follows the same fallback mechanism
     * as {@link #read(String)}.
     *
     * @param path The absolute or relative path to the configuration file
     * @return The loaded configuration instance
     * @throws IllegalArgumentException if the path is null or has an unsupported extension
     * @throws SecurityException if file access is denied by the security manager
     *
     * @author LeyCM
     * @since 2.0.2
     */
    Config read(Path path);

    /**
     * Sets the base configuration directory where configuration files are stored.
     * All relative filenames passed to {@link #read(String)} will be resolved
     * against this directory.
     *
     * @param directory The base directory for configuration files
     * @throws IllegalArgumentException if the directory is null or not a valid directory
     * @throws SecurityException if the directory cannot be accessed due to security restrictions
     *
     * @author LeyCM
     * @since 2.0.2
     */
    void setConfigDirectory(Path directory);

    /**
     * Gets the current base configuration directory.
     *
     * @return The current base configuration directory, or null if not set
     *
     * @author LeyCM
     * @since 2.0.2
     */
    Path getConfigDirectory();
}