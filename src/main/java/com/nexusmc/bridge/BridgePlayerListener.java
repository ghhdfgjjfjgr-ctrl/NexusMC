package com.nexusmc.bridge;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class BridgePlayerListener implements Listener {
    private final NexusBridgePlugin plugin;
    private final BedrockBridgeService bridgeService;

    public BridgePlayerListener(NexusBridgePlugin plugin, BedrockBridgeService bridgeService) {
        this.plugin = plugin;
        this.bridgeService = bridgeService;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!bridgeService.getGeyserSupport().isBedrockPlayer(event.getPlayer())) {
            return;
        }

        event.getPlayer().sendMessage("§b[NexusBridge] §fเปิดใช้งาน Bedrock bridge แล้ว");
        if (plugin.getConfig().getBoolean("bridge.show-mapping-stats-on-join", true)) {
            event.getPlayer().sendMessage("§7Mapped items: §f" + bridgeService.getMappingCount());
        }
    }
}
