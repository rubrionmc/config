package net.rubrion.config.api.field;

import lombok.Getter;
import net.rubrion.config.api.ConfigApi;
import net.rubrion.config.api.ConfigApiProvider;
import net.rubrion.config.api.ConfigFile;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;

@Getter
public class ConfigField<T> {

    private final String filePath;
    private final String key;
    private final Class<T> type;
    private T defaultValue;
    private Consumer<T> onChange;

    public ConfigField(String filePath, String key, Class<T> type) {
        this.filePath = filePath;
        this.key = key;
        this.type = type;
    }

    public ConfigField<T> withDefault(T defaultValue) {
        this.defaultValue = defaultValue;
        ensureDefault();
        return this;
    }

    public ConfigField<T> onChange(Consumer<T> listener) {
        this.onChange = listener;
        return this;
    }

    public T get() {
        ConfigFile file = api().getFile(filePath);
        return file.get(key, type).orElse(defaultValue);
    }

    public void set(T value) {
        ConfigFile file = api().getFile(filePath);
        file.set(key, value);
        file.save();

        if (onChange != null) {
            onChange.accept(value);
        }
    }

    public boolean exists() {
        return api().getFile(filePath).exists(key);
    }

    public void delete() {
        ConfigFile file = api().getFile(filePath);
        file.delete(key);
        file.save();
    }

    public void reset() {
        if (defaultValue != null) {
            set(defaultValue);
        } else {
            delete();
        }
    }

    public Optional<T> getOptional() {
        return api().getFile(filePath).get(key, type);
    }

    public T getOrDefault(T fallback) {
        return getOptional().orElse(fallback);
    }

    public void reload() {
        api().reload(filePath);
    }

    private void ensureDefault() {
        if (defaultValue != null && !exists()) {
            set(defaultValue);
        }
    }

    private @NotNull ConfigApi api() {
        return ConfigApiProvider.get();
    }
}