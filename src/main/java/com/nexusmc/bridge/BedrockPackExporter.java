package com.nexusmc.bridge;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public final class BedrockPackExporter {
    public ExportResult export(Path outputDir, Iterable<ItemMapping> mappings) {
        try {
            Files.createDirectories(outputDir);

            Path manifestPath = outputDir.resolve("manifest.json");
            Path itemTexturePath = outputDir.resolve("textures").resolve("item_texture.json");
            Files.createDirectories(itemTexturePath.getParent());

            String manifest = buildManifestJson();
            String itemTexture = buildItemTextureJson(mappings);

            Files.writeString(manifestPath, manifest, StandardCharsets.UTF_8);
            Files.writeString(itemTexturePath, itemTexture, StandardCharsets.UTF_8);

            return ExportResult.success(outputDir, countMappings(mappings));
        } catch (IOException e) {
            return ExportResult.failure(e.getMessage());
        }
    }

    private int countMappings(Iterable<ItemMapping> mappings) {
        int count = 0;
        for (ItemMapping ignored : mappings) {
            count++;
        }
        return count;
    }

    private String buildManifestJson() {
        String headerUuid = UUID.randomUUID().toString();
        String moduleUuid = UUID.randomUUID().toString();
        return """
                {
                  \"format_version\": 2,
                  \"header\": {
                    \"description\": \"NexusMC Bedrock bridge texture pack\",
                    \"name\": \"NexusMC Bridge Pack\",
                    \"uuid\": \"%s\",
                    \"version\": [1, 0, 0],
                    \"min_engine_version\": [1, 20, 0]
                  },
                  \"modules\": [
                    {
                      \"description\": \"NexusMC bridge texture module\",
                      \"type\": \"resources\",
                      \"uuid\": \"%s\",
                      \"version\": [1, 0, 0]
                    }
                  ]
                }
                """.formatted(headerUuid, moduleUuid);
    }

    private String buildItemTextureJson(Iterable<ItemMapping> mappings) {
        StringBuilder textures = new StringBuilder();
        boolean first = true;
        for (ItemMapping mapping : mappings) {
            if (mapping.texturePath() == null || mapping.texturePath().isBlank()) {
                continue;
            }

            if (!first) {
                textures.append(",\n");
            }

            textures.append("    \"")
                    .append(mapping.bedrockId())
                    .append("\": { \"textures\": \"")
                    .append(mapping.texturePath().replace(".png", ""))
                    .append("\" }");
            first = false;
        }

        return """
                {
                  \"resource_pack_name\": \"nexusmc_bridge_pack\",
                  \"texture_name\": \"atlas.items\",
                  \"texture_data\": {
                %s
                  }
                }
                """.formatted(textures);
    }
}
