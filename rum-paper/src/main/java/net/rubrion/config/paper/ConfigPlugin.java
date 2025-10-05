package net.rubrion.config.paper;

import net.rubrion.config.api.ConfigApiProvider;
import net.rubrion.config.common.ConfigBootstrap;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigPlugin extends JavaPlugin {

    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {
        ConfigApiProvider.register(new ConfigBootstrap());
    }

    @Override
    public void onDisable() {
        ConfigApiProvider.unregister();
    }
}
