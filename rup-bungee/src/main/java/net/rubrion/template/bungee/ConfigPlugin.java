package net.rubrion.template.bungee;

import net.md_5.bungee.api.plugin.Plugin;
import net.rubrion.config.api.ConfigApiProvider;
import net.rubrion.config.common.ConfigBootstrap;

public class ConfigPlugin extends Plugin {

    @Override
    public void onEnable() {
        ConfigApiProvider.register(new ConfigBootstrap());
    }

    @Override
    public void onDisable() {
        ConfigApiProvider.unregister();
    }

}
