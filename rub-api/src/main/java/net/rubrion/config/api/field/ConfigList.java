package net.rubrion.config.api.field;

import net.rubrion.config.api.ConfigApi;
import net.rubrion.config.api.ConfigApiProvider;
import net.rubrion.config.api.ConfigFile;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ConfigList<T> implements Iterable<T> {

    private static final ScheduledExecutorService SCHEDULER = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread thread = new Thread(r, "ConfigList-Scheduler");
        thread.setDaemon(true);
        return thread;
    });

    private final String filePath;
    private final String key;
    private final Class<T> elementType;
    private List<T> cache;
    private Consumer<List<T>> onChange;
    private volatile boolean initialized = false;

    public ConfigList(String filePath, String key, Class<T> elementType) {
        this.filePath = filePath;
        this.key = key;
        this.elementType = elementType;
        this.cache = new ArrayList<>();
        initialize();
    }

    private void initialize() {
        SCHEDULER.scheduleAtFixedRate(() -> {
            if (!initialized && apiAvailable()) {
                reload();
                initialized = true;
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public ConfigList<T> onChange(Consumer<List<T>> listener) {
        this.onChange = listener;
        return this;
    }

    public void add(T element) {
        waitForApi();
        cache.add(element);
        save();
    }

    public void add(int index, T element) {
        waitForApi();
        cache.add(index, element);
        save();
    }

    public boolean remove(T element) {
        waitForApi();
        boolean removed = cache.remove(element);
        if (removed) save();
        return removed;
    }

    public T remove(int index) {
        waitForApi();
        T removed = cache.remove(index);
        save();
        return removed;
    }

    public void set(int index, T element) {
        waitForApi();
        cache.set(index, element);
        save();
    }

    public T get(int index) {
        waitForApi();
        return cache.get(index);
    }

    public int size() {
        waitForApi();
        return cache.size();
    }

    public boolean isEmpty() {
        waitForApi();
        return cache.isEmpty();
    }

    public boolean contains(T element) {
        waitForApi();
        return cache.contains(element);
    }

    public void clear() {
        waitForApi();
        cache.clear();
        save();
    }

    public List<T> getAll() {
        waitForApi();
        return new ArrayList<>(cache);
    }

    public void setAll(List<T> elements) {
        waitForApi();
        cache = new ArrayList<>(elements);
        save();
    }

    public void reload() {
        waitForApi();
        ConfigFile file = api().getFile(filePath);
        cache = new ArrayList<>(file.getList(key, elementType));
    }

    public void save() {
        waitForApi();
        ConfigFile file = api().getFile(filePath);
        file.set(key, cache);
        file.save();

        if (onChange != null) {
            onChange.accept(new ArrayList<>(cache));
        }
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        waitForApi();
        return cache.iterator();
    }

    public void forEach(Consumer<? super T> action) {
        waitForApi();
        cache.forEach(action);
    }

    public List<T> subList(int fromIndex, int toIndex) {
        waitForApi();
        return cache.subList(fromIndex, toIndex);
    }

    public int indexOf(T element) {
        waitForApi();
        return cache.indexOf(element);
    }

    private void waitForApi() {
        if (!initialized || !apiAvailable()) {
            CompletableFuture<Void> future = new CompletableFuture<>();
            SCHEDULER.scheduleAtFixedRate(() -> {
                if (apiAvailable()) {
                    if (!initialized) {
                        reload();
                        initialized = true;
                    }
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