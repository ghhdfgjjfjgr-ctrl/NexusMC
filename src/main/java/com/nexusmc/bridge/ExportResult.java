package com.nexusmc.bridge;

import java.nio.file.Path;

public record ExportResult(boolean success, Path outputDir, int exportedMappings, String error) {
    public static ExportResult success(Path outputDir, int exportedMappings) {
        return new ExportResult(true, outputDir, exportedMappings, "");
    }

    public static ExportResult failure(String error) {
        return new ExportResult(false, null, 0, error);
    }
}
