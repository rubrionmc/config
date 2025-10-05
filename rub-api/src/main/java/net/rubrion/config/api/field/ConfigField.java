package net.rubrion.config.api.field;

import lombok.Getter;
import net.rubrion.config.api.ConfigApi;
import net.rubrion.config.api.ConfigApiProvider;
import net.rubrion.config.api.ConfigFile;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Getter
public class ConfigField<T> {

    private static final ScheduledExecutorService SCHEDULER = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread thread = new Thread(r, "ConfigField-Scheduler");
        thread.setDaemon(true);
        return thread;
    });

    private final String filePath;
    private final String key;
    private final Class<T> type;
    private T defaultValue;
    private Consumer<T> onChange;
    private volatile boolean initialized = false;

    public ConfigField(String filePath, String key, Class<T> type) {
        this.filePath = filePath;
        this.key = key;
        this.type = type;
        initialize();
    }

    private void initialize() {
        SCHEDULER.scheduleAtFixedRate(() -> {
            if (!initialized && apiAvailable()) {
                ensureDefault();
                initialized = true;
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public ConfigField<T> withDefault(T defaultValue) {
        this.defaultValue = defaultValue;
        if (apiAvailable()) {
            ensureDefault();
        }
        return this;
    }

    public ConfigField<T> onChange(Consumer<T> listener) {
        this.onChange = listener;
        return this;
    }

    public T get() {
        waitForApi();
        ConfigFile file = api().getFile(filePath);
        return file.get(key, type).orElse(defaultValue);
    }

    public void set(T value) {
        waitForApi();
        ConfigFile file = api().getFile(filePath);
        file.set(key, value);
        file.save();

        if (onChange != null) {
            onChange.accept(value);
        }
    }

    public boolean exists() {
        waitForApi();
        return api().getFile(filePath).exists(key);
    }

    public void delete() {
        waitForApi();
        ConfigFile file = api().getFile(filePath);
        file.delete(key);
        file.save();
    }

    public void reset() {
        waitForApi();
        if (defaultValue != null) {
            set(defaultValue);
        } else {
            delete();
        }
    }

    public Optional<T> getOptional() {
        waitForApi();
        return api().getFile(filePath).get(key, type);
    }

    public T getOrDefault(T fallback) {
        waitForApi();
        return getOptional().orElse(fallback);
    }

    public void reload() {
        waitForApi();
        api().reload(filePath);
    }

    private void ensureDefault() {
        if (defaultValue != null && apiAvailable() && !exists()) {
            set(defaultValue);
        }
    }

    private void waitForApi() {
        if (!apiAvailable()) {
            CompletableFuture<Void> future = new CompletableFuture<>();
            SCHEDULER.scheduleAtFixedRate(() -> {
                if (apiAvailable()) {
                    future.complete(null);
                }
            }, 0, 100, TimeUnit.MILLISECONDS);
            future.join();
        }
    }

    private boolean apiAvailable() {
        try {
            ConfigApi api = ConfigApiProvider.get();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private @NotNull ConfigApi api() {
        return ConfigApiProvider.get();
    }

    public static void shutdown() {
        SCHEDULER.shutdown();
    }
}