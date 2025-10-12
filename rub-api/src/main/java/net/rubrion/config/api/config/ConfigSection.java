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