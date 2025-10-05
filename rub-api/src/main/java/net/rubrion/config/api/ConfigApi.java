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
package net.rubrion.config.api;


import java.nio.file.Path;
import java.util.Optional;

public interface ConfigApi {

    ConfigFile getFile(String path);

    ConfigFile getFile(Path path);

    void reload(String path);

    void reloadAll();

    void saveAll();

    void unload(String path);

    <T> Optional<T> get(String filePath, String key, Class<T> type);

    <T> void set(String filePath, String key, T value);

    boolean exists(String filePath, String key);

    void delete(String filePath, String key);
}