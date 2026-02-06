package com.nexusmc.bridge.hook;

import com.nexusmc.bridge.HookLoadResult;
import com.nexusmc.bridge.ItemConverterRegistry;
import com.nexusmc.bridge.ItemMapping;
import com.nexusmc.bridge.NexusBridgePlugin;
import com.nexusmc.bridge.PluginHook;
import com.nexusmc.bridge.TexturePackMapper;

public final class ItemsAdderHook implements PluginHook {
    @Override
    public String targetPluginName() {
        return "ItemsAdder";
    }

    @Override
    public HookLoadResult register(NexusBridgePlugin plugin,
                                   ItemConverterRegistry itemRegistry,
                                   TexturePackMapper texturePackMapper) {
        // scaffold: ในระบบจริงควรดึงจาก API ของ ItemsAdder โดยตรง
        ItemMapping sample = new ItemMapping(
                "itemsadder:ruby_sword",
                "bridge:ruby_sword",
                "textures/items/ruby_sword.png",
                10001,
                "itemsadder"
        );
        itemRegistry.register(sample);
        texturePackMapper.registerTexture(sample.javaId(), sample.texturePath());

        return HookLoadResult.loaded(1, "default sample mapping injected");
    }
}
