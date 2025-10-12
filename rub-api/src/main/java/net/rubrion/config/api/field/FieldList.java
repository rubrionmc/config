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
package net.rubrion.config.api.field;

import java.util.List;

/**
 * A reactive field that represents a list of elements. This interface extends {@link Field}
 * to provide list-specific operations while maintaining reactive capabilities.
 *
 * @param <T> the type of elements in the list
 *
 * @author LeyCM
 * @since 2.0.2
 */
public interface FieldList<T> extends Field<List<T>> {
    /**
     * Adds an item to the list. This operation is reactive and will trigger
     * appropriate change notifications to registered listeners.
     *
     * @param item the item to add to the list, cannot be null
     * @throws IllegalArgumentException if the item is null or invalid for the list type
     * @throws UnsupportedOperationException if the list does not support add operations
     *
     * @author LeyCM
     * @since 2.0.2
     */
    void add(T item);

    /**
     * Removes an item from the list. This operation is reactive and will trigger
     * appropriate change notifications to registered listeners.
     *
     * @param item the item to remove from the list
     * @throws UnsupportedOperationException if the list does not support remove operations
     *
     * @author LeyCM
     * @since 2.0.2
     */
    void remove(T item);

    /**
     * Gets the current size of the list.
     *
     * @return the number of elements in the list
     *
     * @author LeyCM
     * @since 2.0.2
     */
    int size();
}