package com.nexusmc.bridge;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class NexusBridgeCommand implements CommandExecutor, TabCompleter {
    private final BedrockBridgeService bridgeService;

    public NexusBridgeCommand(BedrockBridgeService bridgeService) {
        this.bridgeService = bridgeService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("status")) {
            sender.sendMessage("§aNexusBridge online");
            sender.sendMessage("§7Geyser: §f" + bridgeService.getGeyserSupport().isGeyserEnabled());
            sender.sendMessage("§7Floodgate: §f" + bridgeService.getGeyserSupport().isFloodgateEnabled());
            sender.sendMessage("§7Active hooks: §f" + bridgeService.getHookSummary());
            sender.sendMessage("§7Mappings loaded: §f" + bridgeService.getMappingCount());
            sender.sendMessage("§7Source stats:");
            for (Map.Entry<String, Integer> sourceStat : bridgeService.getSourceStats().entrySet()) {
                sender.sendMessage("  §8- §f" + sourceStat.getKey() + ": §b" + sourceStat.getValue());
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            bridgeService.reload();
            sender.sendMessage("§aNexusBridge reloaded.");
            return true;
        }

        if (args[0].equalsIgnoreCase("exportpack")) {
            ExportResult result = bridgeService.exportPack();
            if (!result.success()) {
                sender.sendMessage("§cExport failed: " + result.error());
                return true;
            }

            sender.sendMessage("§aExport completed.");
            sender.sendMessage("§7Output: §f" + result.outputDir());
            sender.sendMessage("§7Mappings exported: §f" + result.exportedMappings());
            return true;
        }

        if (args[0].equalsIgnoreCase("map") && args.length >= 2) {
            String javaId = args[1].toLowerCase(Locale.ROOT);
            bridgeService.getRegistry().byJava(javaId).ifPresentOrElse(mapping -> {
                sender.sendMessage("§aMap found");
                sender.sendMessage("§7Java: §f" + mapping.javaId());
                sender.sendMessage("§7Bedrock: §f" + mapping.bedrockId());
                sender.sendMessage("§7Texture: §f" + mapping.texturePath());
                sender.sendMessage("§7Source: §f" + mapping.source());
            }, () -> sender.sendMessage("§cNo mapping for: " + javaId));
            return true;
        }

        sender.sendMessage("§eUsage: /nexusmc <status|reload|exportpack|map <java_id>>");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return List.of("status", "reload", "exportpack", "map");
        }
        return List.of();
    }
}
