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
package net.rubrion.config.common.adapter.config;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import net.rubrion.config.api.adapter.ConfigAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;

/**
 * TOML adapter, preserves comments
 */
public class TomlConfigAdapter implements ConfigAdapter {
    private final TomlWriter writer;

    public TomlConfigAdapter() {
        this.writer = new TomlWriter();
    }

    @Override
    public Map<String, Object> read(String content) throws IOException {
        if (content == null || content.trim().isEmpty()) {
            return new LinkedHashMap<>();
        }

        try {
            Toml toml = new Toml().read(content);
            return toml.toMap();
        } catch (Exception e) {
            throw new IOException("Invalid TOML", e);
        }
    }

    @Override
    public String write(String current, Map<String, Object> data) {
        List<String> comments = new ArrayList<>();
        if (current != null && !current.trim().isEmpty()) {
            String[] lines = current.split("\n");
            for (String line : lines) {
                if (line.trim().startsWith("#")) {
                    comments.add(line);
                }
            }
        }

        StringBuilder result = new StringBuilder();

        for (String comment : comments) {
            result.append(comment).append("\n");
        }

        if (!comments.isEmpty()) {
            result.append("\n");
        }

        String tomlContent = writer.write(data);
        result.append(tomlContent);

        return result.toString();
    }

    @Override
    public String updateValue(String current, String key, Object value) throws IOException {
        Map<String, Object> data = read(current);
        setNestedValue(data, key, value);
        return write(current, data);
    }

    @Override
    public String[] getSupportedExtensions() {
        return new String[]{"toml"};
    }

    @SuppressWarnings("unchecked")
    private void setNestedValue(Map<String, Object> data, @NotNull String key, Object value) {
        String[] parts = key.split("\\.");
        Map<String, Object> current = data;

        for (int i = 0; i < parts.length - 1; i++) {
            Object next = current.get(parts[i]);
            if (!(next instanceof Map)) {
                Map<String, Object> newMap = new LinkedHashMap<>();
                current.put(parts[i], newMap);
                current = newMap;
            } else {
                current = (Map<String, Object>) next;
            }
        }

        current.put(parts[parts.length - 1], value);
    }
}