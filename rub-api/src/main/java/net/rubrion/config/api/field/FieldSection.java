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
