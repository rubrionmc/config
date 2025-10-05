package net.rubrion.config.common.registry;

import net.rubrion.config.api.ConfigFile;
import net.rubrion.config.common.file.FileFormat;
import net.rubrion.config.common.file.JsonConfigFile;
import net.rubrion.config.common.file.YamlConfigFile;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigRegistry {

    private final Map<String, ConfigFile> files = new ConcurrentHashMap<>();
    private final Path baseDirectory;

    public ConfigRegistry() {
        this(Paths.get("./.config"));
    }

    public ConfigRegistry(Path baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    public ConfigFile get(String path) {
        return files.computeIfAbsent(path, this::create);
    }

    private @NotNull ConfigFile create(String path) {
        Path fullPath = baseDirectory.resolve(path);
        FileFormat format = FileFormat.detect(path);

        return switch (format) {
            case YAML -> new YamlConfigFile(fullPath);
            case JSON -> new JsonConfigFile(fullPath);
            default -> throw new IllegalArgumentException("Unsupported file format: " + path);
        };
    }

    public void reload(String path) {
        ConfigFile file = files.get(path);
        if (file != null) {
            file.reload();
        }
    }

    public void reloadAll() {
        files.values().forEach(ConfigFile::reload);
    }

    public void saveAll() {
        files.values().forEach(ConfigFile::save);
    }

    public void unload(String path) {
        files.remove(path);
    }

    public void clear() {
        files.clear();
    }

    public Map<String, ConfigFile> getAll() {
        return Map.copyOf(files);
    }
}
