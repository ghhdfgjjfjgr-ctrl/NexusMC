package com.nexusmc.bridge;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class TexturePackMapper {
    private final Map<String, String> textureByItem = new HashMap<>();

    public void registerTexture(String itemId, String texturePath) {
        textureByItem.put(itemId, texturePath);
    }

    public Optional<String> resolveTexture(String itemId) {
        return Optional.ofNullable(textureByItem.get(itemId));
    }

    public int size() {
        return textureByItem.size();
    }

    public Map<String, String> snapshot() {
        return Map.copyOf(textureByItem);
    }

    public void clear() {
        textureByItem.clear();
    }
}
