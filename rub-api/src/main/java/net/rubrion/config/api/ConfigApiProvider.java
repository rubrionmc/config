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
package net.rubrion.config.api;

import net.rubrion.common.api.api.ApiProvider;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * <p>
 * Provides static access to the {@link ConfigApiModule} instance.
 * This allows users to retrieve the API implementation
 * without directly depending on the core implementation.
 * </p>
 */
public class ConfigApiProvider implements ApiProvider {
    private static ConfigApiModule instance = null;

    private ConfigApiProvider() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }

    /**
     * Returns the current {@link ConfigApiModule} API instance.
     *
     * @return the registered API instance
     * @throws NotLoadedException if no API implementation is registered
     */
    public static @NotNull ConfigApiModule get() {
        ConfigApiModule inst = instance;
        if (inst == null) {
            throw new NotLoadedException();
        }
        return inst;
    }

    /**
     * Registers the API implementation.
     * This method should be called by the core module at startup.
     * @throws IllegalStateException if an API implementation is registered
     *
     * @param api the API implementation
     */
    @ApiStatus.Internal
    public static void register(ConfigApiModule api) {
        if (instance != null) {
            throw new IllegalStateException("Template API already registered");
        }
        instance = api;
    }

    /**
     * Unregisters the API implementation.
     * This should be called during shutdown or when the implementation is no longer valid.
     */
    @ApiStatus.Internal
    public static void unregister() {
        instance = null;
    }

    /**
     * Exception thrown when the API is requested before it has been loaded.
     */
    private static final class NotLoadedException extends IllegalStateException {
        private static final String MESSAGE = """
                The Template API isn't loaded yet!
                Possible reasons:
                  a) Template plugin not started or failed to load
                  b) Accessing TemplateApiProvider.get() too early
                """;

        NotLoadedException() {
            super(MESSAGE);
        }

    }
}
