package net.rubrion.config.api.field;

/**
 * Reactive field that always reflects the current config value
 */
public interface Field<T> {
    /**
     * Gets the current value
     */
    T get();

    /**
     * Sets the value (auto-saves)
     */
    void set(T value);

    /**
     * Gets the value or default
     */
    T getOr(T defaultValue);

    /**
     * Checks if the value exists
     */
    boolean exists();

    /**
     * Gets the key this field represents
     */
    String key();
}
