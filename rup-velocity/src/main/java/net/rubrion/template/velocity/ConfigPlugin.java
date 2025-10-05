package net.rubrion.template.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import net.rubrion.config.api.ConfigApiProvider;
import net.rubrion.config.common.ConfigBootstrap;

@Plugin(id = "config", name = "Config", version = "${version}", authors = "${authors}")
public class ConfigPlugin {

    @Inject
    public void onEnable() {
        ConfigApiProvider.register(new ConfigBootstrap());
    }

    @Inject
    public void onDisable() {
        ConfigApiProvider.unregister();
    }

}
