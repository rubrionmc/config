package net.rubrion.config.api.config;

import java.util.List;
import java.util.Optional;

/**
 * Represents a configuration section (nested object)
 */
public interface ConfigSection {
    <T> Optional<T> get(String key, Class<T> type);
    <T> T getOr(String key, Class<T> type, T defaultValue);
    void set(String key, Object value);
    List<String> getKeys();
}