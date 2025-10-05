package net.rubrion.template.spigot;

import net.rubrion.config.api.ConfigApiProvider;
import net.rubrion.config.common.ConfigBootstrap;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Path;

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