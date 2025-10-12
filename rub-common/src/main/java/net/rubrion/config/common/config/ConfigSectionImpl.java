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

import net.rubrion.config.api.config.ConfigSection;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public record ConfigSectionImpl(ConfigImpl config,
                                String prefix) implements ConfigSection {

    @Override
    public <T> Optional<T> get(String key, Class<T> type) {
        return config.get(prefix + "." + key, type);
    }

    @Override
    public <T> T getOr(String key, Class<T> type, T defaultValue) {
        return config.getOr(prefix + "." + key, type, defaultValue);
    }

    @Override
    public void set(String key, Object value) {
        config.set(prefix + "." + key, value);
    }

    @Override
    public @NotNull List<String> getKeys() {
        @SuppressWarnings("unchecked")
        Map<String, Object> value = config.get(prefix, Map.class).orElse(null);
        if (value != null) {
            return new ArrayList<>(value.keySet());
        }
        return Collections.emptyList();
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getData() {
        return (Map<String, Object>) config.get(prefix, Map.class).orElse(new HashMap<>());
    }
}
