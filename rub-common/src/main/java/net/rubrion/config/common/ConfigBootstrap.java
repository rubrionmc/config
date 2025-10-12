package net.rubrion.config.common;

import lombok.Getter;
import net.rubrion.common.api.id.NamespacedId;
import net.rubrion.config.api.ConfigApiModule;
import net.rubrion.config.api.config.Config;
import net.rubrion.config.api.config.ConfigFactory;
import net.rubrion.config.common.adapter.type.TypeAdapterRegistryImpl;
import net.rubrion.config.common.config.ConfigFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;

@Getter
public class ConfigBootstrap implements ConfigApiModule {

    private static final Logger DEFAULT_LOGGER = LoggerFactory.getLogger(ConfigBootstrap.class);
    private static final NamespacedId DEFAULT_ID = new NamespacedId("rubrion:config");

    private final Logger logger;
    private final NamespacedId id;
    private final ConfigFactory configFactory;
    private final TypeAdapterRegistryImpl typeAdapterRegistry;

    /**
     * Creates a new ConfigBootstrap using the default directory ("config/"),
     * default NamespacedId ("rubrion:config"), and the default logger.
     */
    public ConfigBootstrap() {
        this(Paths.get("config"), DEFAULT_ID, DEFAULT_LOGGER);
    }

    /**
     * Creates a new ConfigBootstrap using a custom config directory.
     * Uses the default ID and logger.
     *
     * @param configDirectory The configuration directory.
     */
    public ConfigBootstrap(Path configDirectory) {
        this(configDirectory, DEFAULT_ID, DEFAULT_LOGGER);
    }

    /**
     * Creates a new ConfigBootstrap with all parameters provided.
     *
     * @param configDirectory The configuration directory
     * @param id              The namespace id of the loader
     * @param logger          The logger to use
     */
    public ConfigBootstrap(Path configDirectory, NamespacedId id, Logger logger) {
        this.logger = logger != null ? logger : DEFAULT_LOGGER;
        this.id = id != null ? id : DEFAULT_ID;

        this.logger.info("Initializing Config System at: {}", configDirectory);

        this.typeAdapterRegistry = new TypeAdapterRegistryImpl();
        this.configFactory = new ConfigFactoryImpl(configDirectory, typeAdapterRegistry);

        this.logger.info("Config System initialized successfully");
    }

    @Override
    public NamespacedId loader() {
        return id;
    }

    @Override
    public Logger logger() {
        return logger;
    }

    @Override
    public Config read(String filename) {
        return configFactory.read(filename);
    }

    @Override
    public Config read(Path path) {
        return configFactory.read(path);
    }
}
