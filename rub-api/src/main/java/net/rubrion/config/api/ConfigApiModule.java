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

import net.rubrion.common.api.api.ApiModule;
import net.rubrion.config.api.config.Config;

import java.nio.file.Path;

public interface ConfigApiModule extends ApiModule {

    Config read(String filename);

    Config read(Path path);

}