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
package net.rubrion.config.api;

import net.rubrion.common.api.api.ApiModule;
import net.rubrion.config.api.config.Config;
import net.rubrion.config.api.exception.ConfigReadException;

import java.nio.file.Path;

/**
 * API module for configuration management operations.
 * Provides methods to read configuration files from various sources.
 *
 * @author LeyCM
 * @since 2.0.2
 */
public interface ConfigApiModule extends ApiModule {

    /**
     * Reads a configuration file from the specified filename.
     * The file path is resolved relative to the application's configuration directory.
     *
     * @param filename the name of the configuration file to read
     * @return the loaded {@link Config} object containing the configuration data
     * @throws ConfigReadException if the file cannot be read or parsed
     * @throws IllegalArgumentException if the filename is null or empty
     *
     * @author LeyCM
     * @since 2.0.2
     */
    Config read(String filename);

    /**
     * Reads a configuration file from the specified file path.
     * Provides more flexibility in specifying the exact location of the configuration file.
     *
     * @param path the full path to the configuration file to read
     * @return the loaded {@link Config} object containing the configuration data
     * @throws ConfigReadException if the file cannot be read or parsed
     * @throws IllegalArgumentException if the path is null or invalid
     *
     * @author LeyCM
     * @since 2.0.2
     */
    Config read(Path path);

}