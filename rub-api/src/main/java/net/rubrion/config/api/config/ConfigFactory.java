package net.rubrion.config.api.config;

import net.rubrion.config.api.adapter.TypeAdapter;

import java.nio.file.Path;

/**
 * Factory for creating and reading configurations
 */
public interface ConfigFactory {

    /**
     * Reads a config file. If it doesn't exist:
     * 1. Try to copy from resources
     * 2. If not in resources, create empty file
     * <p>
     * Format is detected from file extension (.json, .yml, .yaml, .toml)
     *
     * @param filename The filename relative to the config directory
     * @return The loaded configuration
     */
    Config read(String filename);

    /**
     * Reads a config with explicit path
     */
    Config read(Path path);

    /**
     * Sets the base config directory
     */
    void setConfigDirectory(Path directory);

    /**
     * Gets the base config directory
     */
    Path getConfigDirectory();
}