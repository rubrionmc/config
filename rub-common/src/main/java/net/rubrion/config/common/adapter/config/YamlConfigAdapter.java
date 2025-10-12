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

import net.rubrion.config.api.adapter.ConfigAdapter;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.util.*;

/**
 * YAML adapter using SnakeYAML, preserves comments
 */
public class YamlConfigAdapter implements ConfigAdapter {
    private final Yaml yaml;

    public YamlConfigAdapter() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        options.setIndent(2);
        this.yaml = new Yaml(options);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> read(String content) throws IOException {
        if (content == null || content.trim().isEmpty()) {
            return new LinkedHashMap<>();
        }

        try {
            Object loaded = yaml.load(content);
            if (loaded instanceof Map) {
                return (Map<String, Object>) loaded;
            }
            return new LinkedHashMap<>();
        } catch (Exception e) {
            throw new IOException("Invalid YAML", e);
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

        String yamlContent = yaml.dump(data);
        result.append(yamlContent);

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
        return new String[]{"yml", "yaml"};
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