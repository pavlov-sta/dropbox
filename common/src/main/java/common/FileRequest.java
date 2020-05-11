package common;


public class FileRequest extends AbstractMessage {
    private String filename;
    private String operationType;
    private String listName;

    public String getFilename() {
        return filename;
    }

    public String getOperationType() {
        return operationType;
    }

    public String getListViewName() {
        return listName;
    }

    public FileRequest(String filename, String operationType, String listName) {
        this.listName = listName;
        this.filename = filename;
        this.operationType = operationType;
    }
}
