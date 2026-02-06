package com.nexusmc.bridge;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public final class BridgeConfig {
    private final boolean compatibilityMode;
    private final boolean autoDetectHooks;
    private final List<ItemMapping> staticMappings;

    private BridgeConfig(boolean compatibilityMode, boolean autoDetectHooks, List<ItemMapping> staticMappings) {
        this.compatibilityMode = compatibilityMode;
        this.autoDetectHooks = autoDetectHooks;
        this.staticMappings = staticMappings;
    }

    public boolean compatibilityMode() {
        return compatibilityMode;
    }

    public boolean autoDetectHooks() {
        return autoDetectHooks;
    }

    public List<ItemMapping> staticMappings() {
        return staticMappings;
    }

    public static BridgeConfig from(FileConfiguration config) {
        boolean compatibilityMode = config.getBoolean("bridge.compatibility-mode", true);
        boolean autoDetectHooks = config.getBoolean("bridge.auto-detect-hooks", true);

        List<ItemMapping> mappings = new ArrayList<>();
        ConfigurationSection section = config.getConfigurationSection("mappings.items");
        if (section != null) {
            for (String javaId : section.getKeys(false)) {
                ConfigurationSection row = section.getConfigurationSection(javaId);
                if (row == null) {
                    continue;
                }

                String bedrockId = row.getString("bedrock", javaId);
                String texture = row.getString("texture", "");
                Integer modelData = row.contains("custom-model-data") ? row.getInt("custom-model-data") : null;
                mappings.add(new ItemMapping(javaId, bedrockId, texture, modelData, "config"));
            }
        }

        return new BridgeConfig(compatibilityMode, autoDetectHooks, mappings);
    }
}
