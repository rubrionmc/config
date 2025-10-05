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
package net.rubrion.config.api.adapter;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class ConfigAdapterRegistry {

    private static final Map<Class<?>, TypeAdapter<?>> ADAPTERS = new HashMap<>();

    public static <T> void register(Class<T> type, TypeAdapter<T> adapter) {
        ADAPTERS.put(type, adapter);
    }

    @SuppressWarnings("unchecked")
    public static <T> @NotNull Optional<TypeAdapter<T>> getAdapter(Class<T> type) {
        return Optional.ofNullable((TypeAdapter<T>) ADAPTERS.get(type));
    }
}

