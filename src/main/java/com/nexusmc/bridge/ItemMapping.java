package com.nexusmc.bridge;

public record ItemMapping(
        String javaId,
        String bedrockId,
        String texturePath,
        Integer customModelData,
        String source
) {
}
