package net.rubrion.config.common.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.rubrion.config.api.ConfigFile;
import net.rubrion.config.api.adapter.Adapters;
import net.rubrion.config.api.adapter.TypeAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class JsonConfigFile implements ConfigFile {

    private final Path path;
    private final Gson gson;
    private Map<String, Object> data;
    private boolean loaded;

    public JsonConfigFile(Path path) {
        this.path = path;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.data = new LinkedHashMap<>();
        this.loaded = false;
        load();
    }

    @Override
    public String getPath() {
        return path.toString();
    }

    @Override
    public void reload() {
        load();
    }

    private void load() {
        if (!Files.exists(path)) {
            copyFromResources();
            if (!Files.exists(path)) {
                data = new LinkedHashMap<>();
                loaded = true;
                return;
            }
        }

        try (Reader reader = Files.newBufferedReader(path)) {
            TypeToken<Map<String, Object>> token = new TypeToken<>() {};
            Map<String, Object> loaded = gson.fromJson(reader, token.getType());
            data = loaded != null ? new LinkedHashMap<>(loaded) : new LinkedHashMap<>();
            this.loaded = true;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config: " + path, e);
        }
    }

    private void copyFromResources() {
        String resourcePath = "config/" + path.getFileName().toString();

        try (InputStream in = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (in == null) return;

            Files.createDirectories(path.getParent());
            Files.copy(in, path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save() {
        try {
            Files.createDirectories(path.getParent());
            try (Writer writer = Files.newBufferedWriter(path)) {
                gson.toJson(data, writer);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save config: " + path, e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> get(String key, Class<T> type) {
        Object value = getValue(key);
        if (value == null) {
            return Optional.empty();
        }

        if (type.isInstance(value)) {
            return Optional.of((T) value);
        }

        TypeAdapter<T> adapter = Adapters.get(type);
        if (adapter != null) {
            return Optional.of(adapter.deserialize(value, type));
        }

        return Optional.empty();
    }

    @Override
    public <T> T get(String key, Class<T> type, T defaultValue) {
        return get(key, type).orElse(defaultValue);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void set(@NotNull String key, Object value) {
        String[] parts = key.split("\\.");
        Map<String, Object> current = data;

        for (int i = 0; i < parts.length - 1; i++) {
            current = (Map<String, Object>) current.computeIfAbsent(parts[i], k -> new LinkedHashMap<>());
        }

        current.put(parts[parts.length - 1], value);
    }

    @Override
    public boolean exists(String key) {
        return getValue(key) != null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void delete(@NotNull String key) {
        String[] parts = key.split("\\.");
        Map<String, Object> current = data;

        for (int i = 0; i < parts.length - 1; i++) {
            Object next = current.get(parts[i]);
            if (!(next instanceof Map)) return;
            current = (Map<String, Object>) next;
        }

        current.remove(parts[parts.length - 1]);
    }

    @Override
    public Set<String> getKeys() {
        return collectKeys(data, "");
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<String> getKeys(String section) {
        Object value = getValue(section);
        if (value instanceof Map) {
            return collectKeys((Map<String, Object>) value, section.isEmpty() ? "" : section + ".");
        }
        return Collections.emptySet();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> getSection(String section) {
        Object value = getValue(section);
        if (value instanceof Map) {
            return new LinkedHashMap<>((Map<String, Object>) value);
        }
        return Collections.emptyMap();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> getList(String key, Class<T> elementType) {
        Object value = getValue(key);
        if (!(value instanceof List<?> rawList)) {
            return new ArrayList<>();
        }

        TypeAdapter<T> adapter = Adapters.get(elementType);

        if (adapter != null) {
            return rawList.stream()
                    .map(item -> adapter.deserialize(item, elementType))
                    .collect(Collectors.toList());
        }

        return rawList.stream()
                .filter(elementType::isInstance)
                .map(item -> (T) item)
                .collect(Collectors.toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K, V> Map<K, V> getMap(String key, Class<K> keyType, Class<V> valueType) {
        Object value = getValue(key);
        if (!(value instanceof Map<?, ?> rawMap)) {
            return new LinkedHashMap<>();
        }

        Map<K, V> result = new LinkedHashMap<>();

        TypeAdapter<K> keyAdapter = Adapters.get(keyType);
        TypeAdapter<V> valueAdapter = Adapters.get(valueType);

        for (Map.Entry<?, ?> entry : rawMap.entrySet()) {
            K k = keyAdapter != null
                    ? keyAdapter.deserialize(entry.getKey(), keyType)
                    : (K) entry.getKey();
            V v = valueAdapter != null
                    ? valueAdapter.deserialize(entry.getValue(), valueType)
                    : (V) entry.getValue();
            result.put(k, v);
        }

        return result;
    }

    @Override
    public void setDefault(String key, Object value) {
        if (!exists(key)) {
            set(key, value);
        }
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }

    @SuppressWarnings("unchecked")
    private @Nullable Object getValue(@NotNull String key) {
        if (key.isEmpty()) return data;

        String[] parts = key.split("\\.");
        Object current = data;

        for (String part : parts) {
            if (!(current instanceof Map)) return null;
            current = ((Map<String, Object>) current).get(part);
            if (current == null) return null;
        }

        return current;
    }

    @SuppressWarnings("unchecked")
    private @NotNull Set<String> collectKeys(@NotNull Map<String, Object> map, String prefix) {
        Set<String> keys = new HashSet<>();

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = prefix + entry.getKey();
            keys.add(key);

            if (entry.getValue() instanceof Map) {
                keys.addAll(collectKeys((Map<String, Object>) entry.getValue(), key + "."));
            }
        }

        return keys;
    }
}