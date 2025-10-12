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
