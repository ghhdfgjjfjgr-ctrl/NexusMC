package com.nexusmc.bridge;

import com.nexusmc.bridge.hook.ItemsAdderHook;
import com.nexusmc.bridge.hook.OraxenHook;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class BedrockBridgeService {
    private final NexusBridgePlugin plugin;
    private final List<PluginHook> hooks = new ArrayList<>();
    private final ItemConverterRegistry converterRegistry = new ItemConverterRegistry();
    private final TexturePackMapper texturePackMapper = new TexturePackMapper();
    private final GeyserSupport geyserSupport = new GeyserSupport();
    private final BedrockPackExporter packExporter = new BedrockPackExporter();

    public BedrockBridgeService(NexusBridgePlugin plugin) {
        this.plugin = plugin;
    }

    public void bootstrap() {
        hooks.clear();
        registerHook(new ItemsAdderHook());
        registerHook(new OraxenHook());
        loadAll();
    }

    public void reload() {
        plugin.reloadConfig();
        converterRegistry.clear();
        texturePackMapper.clear();
        loadAll();
    }

    public ExportResult exportPack() {
        Path output = plugin.getDataFolder().toPath().resolve("exports").resolve("bedrock-pack");
        return packExporter.export(output, converterRegistry.all());
    }

    public void registerHook(PluginHook hook) {
        hooks.add(hook);
    }

    private void loadAll() {
        BridgeConfig bridgeConfig = BridgeConfig.from(plugin.getConfig());

        for (ItemMapping mapping : bridgeConfig.staticMappings()) {
            converterRegistry.register(mapping);
            if (!mapping.texturePath().isBlank()) {
                texturePackMapper.registerTexture(mapping.javaId(), mapping.texturePath());
            }
        }

        if (!geyserSupport.isGeyserEnabled() || !geyserSupport.isFloodgateEnabled()) {
            if (!bridgeConfig.compatibilityMode()) {
                plugin.getLogger().warning("Geyser/Floodgate missing and compatibility-mode=false. Some features are limited.");
            } else {
                plugin.getLogger().warning("Geyser/Floodgate not fully enabled. Bridge runs in compatibility mode.");
            }
        }

        if (!bridgeConfig.autoDetectHooks()) {
            plugin.getLogger().info("Auto hook detection disabled by config.");
            return;
        }

        for (PluginHook hook : hooks) {
            Plugin target = Bukkit.getPluginManager().getPlugin(hook.targetPluginName());
            if (target == null || !target.isEnabled()) {
                continue;
            }

            HookLoadResult result = hook.register(plugin, converterRegistry, texturePackMapper);
            if (result.loaded()) {
                plugin.getLogger().info("Hook " + hook.targetPluginName() + " loaded, +" + result.mappingCount() + " mappings. " + result.details());
            } else {
                plugin.getLogger().warning("Hook " + hook.targetPluginName() + " skipped: " + result.details());
            }
        }
    }

    public void shutdown() {
        hooks.clear();
        converterRegistry.clear();
        texturePackMapper.clear();
    }

    public Map<String, Integer> getSourceStats() {
        Map<String, Integer> sourceCounts = new HashMap<>();
        for (ItemMapping mapping : converterRegistry.all()) {
            sourceCounts.merge(mapping.source(), 1, Integer::sum);
        }
        return sourceCounts;
    }

    public GeyserSupport getGeyserSupport() {
        return geyserSupport;
    }

    public ItemConverterRegistry getRegistry() {
        return converterRegistry;
    }

    public String getHookSummary() {
        return hooks.stream().map(PluginHook::targetPluginName).collect(Collectors.joining(", "));
    }

    public int getMappingCount() {
        return converterRegistry.size();
    }
}
