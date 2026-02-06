package com.nexusmc.bridge;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;

public final class GeyserSupport {
    public boolean isGeyserEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled("Geyser-Spigot")
                || Bukkit.getPluginManager().isPluginEnabled("Geyser");
    }

    public boolean isFloodgateEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled("floodgate");
    }

    public boolean isBedrockPlayer(Player player) {
        Plugin floodgate = Bukkit.getPluginManager().getPlugin("floodgate");
        if (floodgate == null || !floodgate.isEnabled()) {
            return false;
        }

        try {
            Class<?> apiClass = Class.forName("org.geysermc.floodgate.api.FloodgateApi");
            Method getInstance = apiClass.getMethod("getInstance");
            Object api = getInstance.invoke(null);
            Method isFloodgatePlayer = apiClass.getMethod("isFloodgatePlayer", java.util.UUID.class);
            Object result = isFloodgatePlayer.invoke(api, player.getUniqueId());
            return result instanceof Boolean b && b;
        } catch (Exception ignored) {
            return false;
        }
    }
}
