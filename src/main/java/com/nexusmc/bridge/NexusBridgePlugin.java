package com.nexusmc.bridge;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class NexusBridgePlugin extends JavaPlugin {
    private BedrockBridgeService bridgeService;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        bridgeService = new BedrockBridgeService(this);
        bridgeService.bootstrap();

        NexusBridgeCommand command = new NexusBridgeCommand(bridgeService);
        PluginCommand pluginCommand = getCommand("nexusmc");
        if (pluginCommand != null) {
            pluginCommand.setExecutor(command);
            pluginCommand.setTabCompleter(command);
        }

        getServer().getPluginManager().registerEvents(new BridgePlayerListener(this, bridgeService), this);

        getLogger().info("§d╔══════════════════════════════════════╗");
        getLogger().info("§d║           §fNEXUS MC CORE §d          ║");
        getLogger().info("§d║   §bBedrock ⇄ Java Bridge Online §d   ║");
        getLogger().info("§d║      §7Mappings: §f" + bridgeService.getMappingCount() + " §d             ║");
        getLogger().info("§d╚══════════════════════════════════════╝");
    }

    @Override
    public void onDisable() {
        if (bridgeService != null) {
            bridgeService.shutdown();
        }
    }
}
