package com.nexusmc.bridge;

public interface PluginHook {
    String targetPluginName();

    HookLoadResult register(NexusBridgePlugin plugin,
                            ItemConverterRegistry itemRegistry,
                            TexturePackMapper texturePackMapper);
}
