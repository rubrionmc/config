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
package net.rubrion.config.common.field;

import net.rubrion.config.api.config.ConfigSection;
import net.rubrion.config.api.field.Field;
import net.rubrion.config.api.field.FieldSection;
import net.rubrion.config.common.config.ConfigImpl;
import net.rubrion.config.common.config.ConfigSectionImpl;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public record FieldSectionImpl(ConfigImpl config,
                               String key) implements FieldSection {

    @Contract(" -> new")
    @Override
    public @NotNull ConfigSection get() {
        return new ConfigSectionImpl(config, key);
    }

    @Override
    public void set(ConfigSection value) {
        if (value instanceof ConfigSectionImpl impl) {
            config.set(key, impl.getData());
        }
    }

    @Override
    public ConfigSection getOr(ConfigSection defaultValue) {
        return config.get(key, Map.class).isPresent() ? get() : defaultValue;
    }

    @Override
    public boolean exists() {
        return config.get(key, Map.class).isPresent();
    }

    @Override
    public <T> Field<T> getField(String subKey, Class<T> type) {
        return config.getField(key + "." + subKey, type);
    }

    @Override
    public List<String> getKeys() {
        return get().getKeys();
    }
}
