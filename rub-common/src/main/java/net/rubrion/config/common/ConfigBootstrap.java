package net.rubrion.config.common;

import lombok.Getter;
import net.rubrion.config.api.ConfigApi;
import net.rubrion.config.api.ConfigFile;
import net.rubrion.config.common.registry.ConfigRegistry;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Optional;

@Getter
public class ConfigBootstrap implements ConfigApi {

    private final ConfigRegistry registry;

    public ConfigBootstrap() {
        this.registry = new ConfigRegistry();
    }

    public ConfigBootstrap(Path baseDirectory) {
        this.registry = new ConfigRegistry(baseDirectory);
    }

    @Override
    public ConfigFile getFile(String path) {
        return registry.get(path);
    }

    @Override
    public ConfigFile getFile(@NotNull Path path) {
        return registry.get(path.toString());
    }

    @Override
    public void reload(String path) {
        ConfigFile file = registry.get(path);
        file.reload();
    }

    @Override
    public void reloadAll() {
        registry.reloadAll();
    }

    @Override
    public void saveAll() {
        registry.saveAll();
    }

    @Override
    public void unload(String path) {
        registry.unload(path);
    }

    @Override
    public <T> Optional<T> get(String filePath, String key, Class<T> type) {
        return getFile(filePath).get(key, type);
    }

    @Override
    public <T> void set(String filePath, String key, T value) {
        ConfigFile file = getFile(filePath);
        file.set(key, value);
        file.save();
    }

    @Override
    public boolean exists(String filePath, String key) {
        return getFile(filePath).exists(key);
    }

    @Override
    public void delete(String filePath, String key) {
        ConfigFile file = getFile(filePath);
        file.delete(key);
        file.save();
    }

}