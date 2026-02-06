package com.nexusmc.bridge.hook;

import com.nexusmc.bridge.HookLoadResult;
import com.nexusmc.bridge.ItemConverterRegistry;
import com.nexusmc.bridge.ItemMapping;
import com.nexusmc.bridge.NexusBridgePlugin;
import com.nexusmc.bridge.PluginHook;
import com.nexusmc.bridge.TexturePackMapper;

public final class OraxenHook implements PluginHook {
    @Override
    public String targetPluginName() {
        return "Oraxen";
    }

    @Override
    public HookLoadResult register(NexusBridgePlugin plugin,
                                   ItemConverterRegistry itemRegistry,
                                   TexturePackMapper texturePackMapper) {
        // scaffold: ในระบบจริงควรอ่าน item registry จาก Oraxen API
        ItemMapping sample = new ItemMapping(
                "oraxen:obsidian_blade",
                "bridge:obsidian_blade",
                "textures/items/obsidian_blade.png",
                20001,
                "oraxen"
        );
        itemRegistry.register(sample);
        texturePackMapper.registerTexture(sample.javaId(), sample.texturePath());

        return HookLoadResult.loaded(1, "default sample mapping injected");
    }
}
