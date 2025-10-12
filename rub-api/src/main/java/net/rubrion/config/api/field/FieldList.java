package net.rubrion.config.api.field;

import java.util.List;

/**
 * Reactive field for lists
 */
public interface FieldList<T> extends Field<List<T>> {
    /**
     * Adds an item to the list
     */
    void add(T item);

    /**
     * Removes an item from the list
     */
    void remove(T item);

    /**
     * Gets the size of the list
     */
    int size();
}
