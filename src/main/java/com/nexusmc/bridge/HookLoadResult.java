package com.nexusmc.bridge;

public record HookLoadResult(boolean loaded, int mappingCount, String details) {
    public static HookLoadResult skipped(String details) {
        return new HookLoadResult(false, 0, details);
    }

    public static HookLoadResult loaded(int mappingCount, String details) {
        return new HookLoadResult(true, mappingCount, details);
    }
}
