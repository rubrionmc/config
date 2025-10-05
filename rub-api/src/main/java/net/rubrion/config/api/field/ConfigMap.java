package net.rubrion.config.api.field;

import lombok.Getter;
import net.rubrion.config.api.ConfigApi;
import net.rubrion.config.api.ConfigApiProvider;
import net.rubrion.config.api.ConfigFile;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

@Getter
public class ConfigMap<K, V> implements Map<K, V> {

    private final String filePath;
    private final String key;
    private final Class<K> keyType;
    private final Class<V> valueType;
    private Map<K, V> cache;
    private java.util.function.Consumer<Map<K, V>> onChange;

    public ConfigMap(String filePath, String key, Class<K> keyType, Class<V> valueType) {
        this.filePath = filePath;
        this.key = key;
        this.keyType = keyType;
        this.valueType = valueType;
        reload();
    }

    public ConfigMap<K, V> onChange(java.util.function.Consumer<Map<K, V>> listener) {
        this.onChange = listener;
        return this;
    }

    @Override
    public int size() {
        return cache.size();
    }

    @Override
    public boolean isEmpty() {
        return cache.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return cache.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return cache.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return cache.get(key);
    }

    @Override
    public V put(K key, V value) {
        V old = cache.put(key, value);
        save();
        return old;
    }

    @Override
    public V remove(Object key) {
        V removed = cache.remove(key);
        if (removed != null) save();
        return removed;
    }

    @Override
    public void putAll(@NotNull Map<? extends K, ? extends V> m) {
        cache.putAll(m);
        save();
    }

    @Override
    public void clear() {
        cache.clear();
        save();
    }

    @Override
    public @NotNull Set<K> keySet() {
        return cache.keySet();
    }

    @Override
    public @NotNull Collection<V> values() {
        return cache.values();
    }

    @Override
    public @NotNull Set<Entry<K, V>> entrySet() {
        return cache.entrySet();
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        return cache.getOrDefault(key, defaultValue);
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        cache.forEach(action);
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        cache.replaceAll(function);
        save();
    }

    @Override
    public V putIfAbsent(K key, V value) {
        V old = cache.putIfAbsent(key, value);
        save();
        return old;
    }

    @Override
    public boolean remove(Object key, Object value) {
        boolean removed = cache.remove(key, value);
        if (removed) save();
        return removed;
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        boolean replaced = cache.replace(key, oldValue, newValue);
        if (replaced) save();
        return replaced;
    }

    @Override
    public V replace(K key, V value) {
        V old = cache.replace(key, value);
        save();
        return old;
    }

    @Override
    public V computeIfAbsent(K key, @NotNull Function<? super K, ? extends V> mappingFunction) {
        V result = cache.computeIfAbsent(key, mappingFunction);
        save();
        return result;
    }

    @Override
    public V computeIfPresent(K key, @NotNull BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        V result = cache.computeIfPresent(key, remappingFunction);
        save();
        return result;
    }

    @Override
    public V compute(K key, @NotNull BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        V result = cache.compute(key, remappingFunction);
        save();
        return result;
    }

    @Override
    public V merge(K key, @NotNull V value, @NotNull BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        V result = cache.merge(key, value, remappingFunction);
        save();
        return result;
    }

    public void reload() {
        ConfigFile file = api().getFile(filePath);
        cache = new HashMap<>(file.getMap(key, keyType, valueType));
    }

    public void save() {
        ConfigFile file = api().getFile(filePath);
        file.set(key, cache);
        file.save();
        if (onChange != null) onChange.accept(new HashMap<>(cache));
    }

    private @NotNull ConfigApi api() {
        return ConfigApiProvider.get();
    }
}
