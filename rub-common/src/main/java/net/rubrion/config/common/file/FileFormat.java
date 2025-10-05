package net.rubrion.config.common.file;

import org.jetbrains.annotations.NotNull;

public enum FileFormat {
    YAML,
    JSON,
    TOML,
    XML,
    INI,
    PROPERTIES,
    UNKNOWN;

    public static FileFormat detect(@NotNull String path) {
        String lower = path.toLowerCase();
        String[] split = lower.split("\\.");

        return switch (split[split.length -1]) {
            case "yml", "yaml", "yaml5", "yaml6" -> YAML;
            case "json", "jason", "geojson", "topojson", "gson" -> JSON;
            case "toml", "tml" -> TOML;
            case "xml", "xsd", "xsl", "xslt" -> XML;
            case "ini", "cfg", "conf" -> INI;
            case "properties", "props" -> PROPERTIES;
            default -> UNKNOWN;
        };
    }
}
