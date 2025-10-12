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

import net.rubrion.config.api.adapter.ConfigAdapter;
import net.rubrion.config.api.adapter.TypeAdapterRegistry;
import net.rubrion.config.api.config.Config;
import net.rubrion.config.api.exception.ConfigReadException;
import net.rubrion.config.api.exception.ConfigSaveException;
import net.rubrion.config.api.field.*;
import net.rubrion.config.common.field.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ConfigImpl implements Config {

    private final Path path;
    private final ConfigAdapter adapter;
    private final TypeAdapterRegistry typeRegistry;
    private Map<String, Object> data;
    private String rawContent;

    public ConfigImpl(Path path, ConfigAdapter adapter, TypeAdapterRegistry typeRegistry) {
        this.path = path;
        this.adapter = adapter;
        this.typeRegistry = typeRegistry;
        this.load();
    }

    private void load() {
        try {
            if (!Files.exists(path)) {
                this.rawContent = "";
                this.data = new HashMap<>();
                save();
                return;
            }

            this.rawContent = Files.readString(path);
            this.data = adapter.read(rawContent);
        } catch (IOException e) {
            this.data = new HashMap<>();
            this.rawContent = "";
            throw new ConfigReadException("Failed to load config from " + path, e);
        }
    }

    @Override
    public <T> Optional<T> get(String key, Class<T> type) {
        Object value = getValueByKey(key);
        if (value == null) {
            return Optional.empty();
        }

        try {
            T converted = typeRegistry.convert(value, type);
            return Optional.ofNullable(converted);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public <T> T getOr(String key, Class<T> type, T defaultValue) {
        return get(key, type).orElse(defaultValue);
    }

    @Override
    public <T> Field<T> getField(String key, Class<T> type) {
        return new FieldImpl<>(this, key, type);
    }

    @Override
    public <T> FieldList<T> getFieldList(String key, Class<T> elementType) {
        return new FieldListImpl<>(this, key, elementType);
    }

    @Override
    public FieldSection getFieldSection(String key) {
        return new FieldSectionImpl(this, key);
    }

    @Override
    public void set(String key, Object value) {
        setValueByKey(key, value);
        save();
    }

    @Override
    public void reload() {
        load();
    }

    @Override
    public void save() {
        try {
            Files.createDirectories(path.getParent());
            String updated = adapter.write(rawContent, data);
            Files.writeString(path, updated);
            this.rawContent = updated;
        } catch (IOException e) {
            throw new ConfigSaveException("Failed to save config to " + path, e);
        }
    }

    @Override
    public Path getPath() {
        return path;
    }

    private @Nullable Object getValueByKey(@NotNull String key) {
        String[] parts = key.split("\\.");
        Map<String, Object> current = data;

        for (int i = 0; i < parts.length - 1; i++) {
            Object next = current.get(parts[i]);
            if (!(next instanceof Map)) {
                return null;
            }
            //noinspection unchecked
            current = (Map<String, Object>) next;
        }

        return current.get(parts[parts.length - 1]);
    }

    private void setValueByKey(@NotNull String key, Object value) {
        String[] parts = key.split("\\.");
        Map<String, Object> current = data;

        for (int i = 0; i < parts.length - 1; i++) {
            Object next = current.get(parts[i]);
            if (!(next instanceof Map)) {
                Map<String, Object> newMap = new LinkedHashMap<>();
                current.put(parts[i], newMap);
                current = newMap;
            } else {
                //noinspection unchecked
                current = (Map<String, Object>) next;
            }
        }

        current.put(parts[parts.length - 1], value);
    }

    Map<String, Object> getData() {
        return data;
    }
}
