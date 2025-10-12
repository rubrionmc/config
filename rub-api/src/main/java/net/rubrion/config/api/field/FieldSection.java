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

import java.util.List;

public interface FieldSection extends Field<ConfigSection> {
    /**
     * Gets a field from this section
     */
    <T> Field<T> getField(String key, Class<T> type);

    /**
     * Gets all keys in this section
     */
    List<String> getKeys();
}
