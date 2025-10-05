package net.rubrion.config.api;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface ConfigFile {

    String getPath();

    void reload();

    void save();

    <T> Optional<T> get(String key, Class<T> type);

    <T> T get(String key, Class<T> type, T defaultValue);

    void set(String key, Object value);

    boolean exists(String key);

    void delete(String key);

    Set<String> getKeys();

    Set<String> getKeys(String section);

    Map<String, Object> getSection(String section);

    <T> List<T> getList(String key, Class<T> elementType);

    <K, V> Map<K, V> getMap(String key, Class<K> keyType, Class<V> valueType);

    void setDefault(String key, Object value);

    boolean isLoaded();
}
