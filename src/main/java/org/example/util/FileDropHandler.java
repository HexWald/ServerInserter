package org.example.util;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.util.List;
import java.util.function.Consumer;

public class FileDropHandler extends TransferHandler {

    private final Consumer<File> callback;

    public FileDropHandler(Consumer<File> callback) {
        this.callback = callback;
    }

    @Override
    public boolean canImport(TransferSupport support) {
        return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
    }

    @Override
    public boolean importData(TransferSupport support) {

        try {
            List<File> files = (List<File>) support.getTransferable()
                    .getTransferData(DataFlavor.javaFileListFlavor);

            callback.accept(files.get(0));
            return true;

        } catch (Exception e) {
            return false;
        }
    }
}
