package net.rubrion.config.api.field;

import net.rubrion.config.api.ConfigApi;
import net.rubrion.config.api.ConfigApiProvider;
import net.rubrion.config.api.ConfigFile;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

public class ConfigList<T> implements Iterable<T> {

    private final String filePath;
    private final String key;
    private final Class<T> elementType;
    private List<T> cache;
    private Consumer<List<T>> onChange;

    public ConfigList(String filePath, String key, Class<T> elementType) {
        this.filePath = filePath;
        this.key = key;
        this.elementType = elementType;
        reload();
    }

    public ConfigList<T> onChange(Consumer<List<T>> listener) {
        this.onChange = listener;
        return this;
    }

    public void add(T element) {
        cache.add(element);
        save();
    }

    public void add(int index, T element) {
        cache.add(index, element);
        save();
    }

    public boolean remove(T element) {
        boolean removed = cache.remove(element);
        if (removed) save();
        return removed;
    }

    public T remove(int index) {
        T removed = cache.remove(index);
        save();
        return removed;
    }

    public void set(int index, T element) {
        cache.set(index, element);
        save();
    }

    public T get(int index) {
        return cache.get(index);
    }

    public int size() {
        return cache.size();
    }

    public boolean isEmpty() {
        return cache.isEmpty();
    }

    public boolean contains(T element) {
        return cache.contains(element);
    }

    public void clear() {
        cache.clear();
        save();
    }

    public List<T> getAll() {
        return new ArrayList<>(cache);
    }

    public void setAll(List<T> elements) {
        cache = new ArrayList<>(elements);
        save();
    }

    public void reload() {
        ConfigFile file = api().getFile(filePath);
        cache = new ArrayList<>(file.getList(key, elementType));
    }

    public void save() {
        ConfigFile file = api().getFile(filePath);
        file.set(key, cache);
        file.save();

        if (onChange != null) {
            onChange.accept(new ArrayList<>(cache));
        }
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return cache.iterator();
    }

    public void forEach(Consumer<? super T> action) {
        cache.forEach(action);
    }

    public List<T> subList(int fromIndex, int toIndex) {
        return cache.subList(fromIndex, toIndex);
    }

    public int indexOf(T element) {
        return cache.indexOf(element);
    }

    private @NotNull ConfigApi api() {
        return ConfigApiProvider.get();
    }
}
