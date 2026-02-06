package com.nexusmc.bridge;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class ItemConverterRegistry {
    private final Map<String, ItemMapping> byJavaId = new HashMap<>();
    private final Map<String, ItemMapping> byBedrockId = new HashMap<>();

    public void register(ItemMapping mapping) {
        byJavaId.put(mapping.javaId(), mapping);
        byBedrockId.put(mapping.bedrockId(), mapping);
    }

    public Optional<ItemMapping> byJava(String javaId) {
        return Optional.ofNullable(byJavaId.get(javaId));
    }

    public Optional<ItemMapping> byBedrock(String bedrockId) {
        return Optional.ofNullable(byBedrockId.get(bedrockId));
    }

    public Collection<ItemMapping> all() {
        return Collections.unmodifiableCollection(byJavaId.values());
    }

    public int size() {
        return byJavaId.size();
    }

    public void clear() {
        byJavaId.clear();
        byBedrockId.clear();
    }
}
