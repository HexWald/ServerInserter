package dev.hexwald.serverinserter.service;

import java.io.File;

public record ImportResult(int inserted, int skippedDuplicates, File backupFile) {
}
