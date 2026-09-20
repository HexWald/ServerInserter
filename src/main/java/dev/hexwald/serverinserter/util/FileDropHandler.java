package dev.hexwald.serverinserter.util;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.util.List;
import java.util.function.Consumer;

public class FileDropHandler extends TransferHandler {

    private final Consumer<File> callback;
    private final Consumer<String> errorCallback;

    public FileDropHandler(Consumer<File> callback) {
        this(callback, message -> { });
    }

    public FileDropHandler(Consumer<File> callback, Consumer<String> errorCallback) {
        this.callback = callback;
        this.errorCallback = errorCallback;
    }

    @Override
    public boolean canImport(TransferSupport support) {
        return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
    }

    @Override
    public boolean importData(TransferSupport support) {

        try {
            Object data = support.getTransferable()
                    .getTransferData(DataFlavor.javaFileListFlavor);

            if (!(data instanceof List<?> files) || files.size() != 1) {
                report("Drop one file at a time.");
                return false;
            }

            Object dropped = files.get(0);
            if (!(dropped instanceof File file) || !file.isFile()) {
                report("Drop a regular file, not a folder.");
                return false;
            }

            callback.accept(file);
            return true;

        } catch (Exception e) {
            report("Could not read the dropped file.");
            return false;
        }
    }

    private void report(String message) {
        if (errorCallback != null) {
            errorCallback.accept(message);
        }
    }
}
