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
package net.rubrion.config.api.adapter;

/**
 * Type adapter for converting between config values and Java objects
 */
public interface TypeAdapter<T> {

    /**
     * Converts from config representation to Java object
     */
    T fromConfig(Object configValue);

    /**
     * Converts from Java object to config representation
     */
    Object toConfig(T value);

    /**
     * Gets the Java type this adapter handles
     */
    Class<T> getType();
}
