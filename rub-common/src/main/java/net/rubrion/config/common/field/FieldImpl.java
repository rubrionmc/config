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

import net.rubrion.config.api.field.Field;
import net.rubrion.config.common.config.ConfigImpl;

public record FieldImpl<T>(ConfigImpl config, String key,
                           Class<T> type) implements Field<T> {

    @Override
    public T get() {
        return config.get(key, type).orElse(null);
    }

    @Override
    public void set(T value) {
        config.set(key, value);
    }

    @Override
    public T getOr(T defaultValue) {
        return config.getOr(key, type, defaultValue);
    }

    @Override
    public boolean exists() {
        return config.get(key, type).isPresent();
    }

}
