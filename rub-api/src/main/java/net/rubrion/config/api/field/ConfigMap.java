package net.rubrion.config.api.field;

import lombok.Getter;
import net.rubrion.config.api.ConfigApi;
import net.rubrion.config.api.ConfigApiProvider;
import net.rubrion.config.api.ConfigFile;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

@Getter
public class ConfigMap<K, V> implements Map<K, V> {

    private static final ScheduledExecutorService SCHEDULER = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread thread = new Thread(r, "ConfigMap-Scheduler");
        thread.setDaemon(true);
        return thread;
    });

    private final String filePath;
    private final String key;
    private final Class<K> keyType;
    private final Class<V> valueType;
    private Map<K, V> cache;
    private java.util.function.Consumer<Map<K, V>> onChange;
    private volatile boolean initialized = false;

    public ConfigMap(String filePath, String key, Class<K> keyType, Class<V> valueType) {
        this.filePath = filePath;
        this.key = key;
        this.keyType = keyType;
        this.valueType = valueType;
        this.cache = new HashMap<>();
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

    public ConfigMap<K, V> onChange(java.util.function.Consumer<Map<K, V>> listener) {
        this.onChange = listener;
        return this;
    }

    @Override
    public int size() {
        waitForApi();
        return cache.size();
    }

    @Override
    public boolean isEmpty() {
        waitForApi();
        return cache.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        waitForApi();
        return cache.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        waitForApi();
        return cache.containsValue(value);
    }

    @Override
    public V get(Object key) {
        waitForApi();
        return cache.get(key);
    }

    @Override
    public V put(K key, V value) {
        waitForApi();
        V old = cache.put(key, value);
        save();
        return old;
    }

    @Override
    public V remove(Object key) {
        waitForApi();
        V removed = cache.remove(key);
        if (removed != null) save();
        return removed;
    }

    @Override
    public void putAll(@NotNull Map<? extends K, ? extends V> m) {
        waitForApi();
        cache.putAll(m);
        save();
    }

    @Override
    public void clear() {
        waitForApi();
        cache.clear();
        save();
    }

    @Override
    public @NotNull Set<K> keySet() {
        waitForApi();
        return cache.keySet();
    }

    @Override
    public @NotNull Collection<V> values() {
        waitForApi();
        return cache.values();
    }

    @Override
    public @NotNull Set<Entry<K, V>> entrySet() {
        waitForApi();
        return cache.entrySet();
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        waitForApi();
        return cache.getOrDefault(key, defaultValue);
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        waitForApi();
        cache.forEach(action);
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        waitForApi();
        cache.replaceAll(function);
        save();
    }

    @Override
    public V putIfAbsent(K key, V value) {
        waitForApi();
        V old = cache.putIfAbsent(key, value);
        save();
        return old;
    }

    @Override
    public boolean remove(Object key, Object value) {
        waitForApi();
        boolean removed = cache.remove(key, value);
        if (removed) save();
        return removed;
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        waitForApi();
        boolean replaced = cache.replace(key, oldValue, newValue);
        if (replaced) save();
        return replaced;
    }

    @Override
    public V replace(K key, V value) {
        waitForApi();
        V old = cache.replace(key, value);
        save();
        return old;
    }

    @Override
    public V computeIfAbsent(K key, @NotNull Function<? super K, ? extends V> mappingFunction) {
        waitForApi();
        V result = cache.computeIfAbsent(key, mappingFunction);
        save();
        return result;
    }

    @Override
    public V computeIfPresent(K key, @NotNull BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        waitForApi();
        V result = cache.computeIfPresent(key, remappingFunction);
        save();
        return result;
    }

    @Override
    public V compute(K key, @NotNull BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        waitForApi();
        V result = cache.compute(key, remappingFunction);
        save();
        return result;
    }

    @Override
    public V merge(K key, @NotNull V value, @NotNull BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        waitForApi();
        V result = cache.merge(key, value, remappingFunction);
        save();
        return result;
    }

    public void reload() {
        waitForApi();
        ConfigFile file = api().getFile(filePath);
        cache = new HashMap<>(file.getMap(key, keyType, valueType));
    }

    public void save() {
        waitForApi();
        ConfigFile file = api().getFile(filePath);
        file.set(key, cache);
        file.save();
        if (onChange != null) onChange.accept(new HashMap<>(cache));
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