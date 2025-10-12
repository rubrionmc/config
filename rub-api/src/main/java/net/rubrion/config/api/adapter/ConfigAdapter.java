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
 * Adapter interface for different config formats
 */
public interface ConfigAdapter {

    /**
     * Reads configuration from string
     * @param content The file content
     * @return Parsed configuration as nested Map
     */
    Map<String, Object> read(String content) throws IOException;

    /**
     * Writes configuration to string, preserving structure and comments
     * @param current The current file content (to preserve comments/structure)
     * @param data The data to write
     * @return Formatted string
     */
    String write(String current, Map<String, Object> data) throws IOException;

    /**
     * Updates a single value in the config while preserving everything else
     * @param current The current file content
     * @param key The key to update (dot notation)
     * @param value The new value
     * @return Updated content
     */
    String updateValue(String current, String key, Object value) throws IOException;

    /**
     * Gets the file extensions this adapter supports
     */
    String[] getSupportedExtensions();
}
