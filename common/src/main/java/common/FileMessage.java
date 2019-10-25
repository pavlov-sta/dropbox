package common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileMessage extends AbstractMessage {
    private String filename;
    private byte[] data;
    private String operationType;
    private String listName;

    public String getFilename() {
        return filename;
    }

    public byte[] getData() {
        return data;
    }

    public String getOperationType() {
        return operationType;
    }

    public String getListName() {
        return listName;
    }

    public FileMessage(Path path, String operationType, String listName) throws IOException {
        this.listName = listName;
        this.operationType = operationType;
        filename = path.getFileName().toString();
        data = Files.readAllBytes(path);
    }
}
