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
package net.rubrion.config.common.config;

import net.rubrion.config.api.ConfigApiProvider;
import net.rubrion.config.api.adapter.ConfigAdapter;
import net.rubrion.config.api.adapter.TypeAdapterRegistry;
import net.rubrion.config.api.config.Config;
import net.rubrion.config.api.config.ConfigFactory;
import net.rubrion.config.api.exception.ConfigSaveException;
import net.rubrion.config.common.adapter.config.JsonConfigAdapter;
import net.rubrion.config.common.adapter.config.TomlConfigAdapter;
import net.rubrion.config.common.adapter.config.YamlConfigAdapter;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public class ConfigFactoryImpl implements ConfigFactory {
    private static final Logger LOGGER = ConfigApiProvider.get().logger();

    private Path configDirectory;
    private final Map<String, ConfigAdapter> adapters;
    private final TypeAdapterRegistry typeRegistry;

    public ConfigFactoryImpl(Path configDirectory, TypeAdapterRegistry typeRegistry) {
        this.configDirectory = configDirectory;
        this.typeRegistry = typeRegistry;
        this.adapters = new HashMap<>();

        registerAdapter(new JsonConfigAdapter());
        registerAdapter(new YamlConfigAdapter());
        registerAdapter(new TomlConfigAdapter());
    }

    private void registerAdapter(@NotNull ConfigAdapter adapter) {
        for (String ext : adapter.getSupportedExtensions()) {
            adapters.put(ext.toLowerCase(), adapter);
        }
    }

    @Override
    public Config read(String filename) {
        Path path = configDirectory.resolve(filename);
        return read(path);
    }

    @Override
    public Config read(Path path) {
        if (!Files.exists(path)) {
            ensureFileExists(path);
        }

        String extension = getFileExtension(path);
        ConfigAdapter adapter = adapters.get(extension.toLowerCase());

        if (adapter == null) {
            throw new IllegalArgumentException("No adapter found for extension: " + extension);
        }

        return new ConfigImpl(path, adapter, typeRegistry);
    }

    private void ensureFileExists(@NotNull Path path) {
        try {
            String filename = path.getFileName().toString();
            InputStream resourceStream = getClass().getClassLoader().getResourceAsStream(filename);

            if (resourceStream != null) {
                LOGGER.trace("Copying default config from resources: {}", filename);
                Files.createDirectories(path.getParent());
                Files.copy(resourceStream, path, StandardCopyOption.REPLACE_EXISTING);
                resourceStream.close();
            } else {
                LOGGER.trace("Creating empty config file: {}", path);
                Files.createDirectories(path.getParent());

                String extension = getFileExtension(path);
                String emptyContent = getEmptyContent(extension);
                Files.writeString(path, emptyContent);
            }
        } catch (IOException e) {
            throw new ConfigSaveException("Failed to create config file", e);
        }
    }

    @Contract(pure = true)
    private @NotNull String getEmptyContent(@NotNull String extension) {
        return switch (extension.toLowerCase()) {
            case "json", "jason", "jsn" -> "{}";
            case "yml", "yaml" -> "# Empty Yaml file\n";
            case "tml", "toml" -> "# Empty Toml file\n";
            default -> "";
        };
    }

    private @NotNull String getFileExtension(@NotNull Path path) {
        String filename = path.getFileName().toString();
        int lastDot = filename.lastIndexOf('.');
        if (lastDot > 0 && lastDot < filename.length() - 1)
            return filename.substring(lastDot + 1);

        return "";
    }

    @Override
    public void setConfigDirectory(Path directory) {
        this.configDirectory = directory;
    }

    @Override
    public Path getConfigDirectory() {
        return configDirectory;
    }
}
