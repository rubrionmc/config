
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

import net.rubrion.config.api.config.ConfigSection;
import net.rubrion.config.api.exception.ConfigReadException;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * A reactive field that represents a configuration section. This interface extends
 * {@link Field} to provide section-specific operations for managing nested configuration
 * fields within a hierarchical structure.
 *
 * @author LeyCM
 * @since 2.0.2
 */
public interface FieldSection extends Field<ConfigSection> {

    /**
     * Retrieves a field from this configuration section with the specified key and type.
     * The field is accessed reactively and changes to the field will be propagated.
     *
     * @param <T> the type of the field value
     * @param key the key identifying the field within this section, cannot be null or empty
     * @param type the class type of the field value, cannot be null
     * @return the field instance for the specified key and type
     * @throws IllegalArgumentException if the key is null, empty, or the type is null
     * @throws NoSuchElementException if no field exists with the given key
     * @throws ClassCastException if the field exists but is not of the specified type
     *
     * @author LeyCM
     * @since 2.0.2
     */
    <T> Field<T> getField(String key, Class<T> type);

    /**
     * Gets all keys currently available in this configuration section.
     * The returned list is a snapshot of keys at the time of invocation.
     *
     * @return a list containing all keys in this section, never null but possibly empty
     *
     * @author LeyCM
     * @since 2.0.2
     */
    List<String> getKeys();
}