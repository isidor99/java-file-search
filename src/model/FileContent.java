package model;

public class FileContent {

    private String filename;
    private String lineNumber;
    private String lineString;

    public FileContent(String filename, String lineNumber, String lineString) {
        setFilename(filename);
        setLineNumber(lineNumber);
        setLineString(lineString);
    }

    public void setFilename(String filename) { this.filename = filename; }

    public String getFilename() { return filename; }

    public void setLineNumber(String lineNumber) { this.lineNumber = lineNumber; }

    public String getLineNumber() { return lineNumber; }

    public void setLineString(String lineString) { this.lineString = lineString; }

    public String getLineString() { return lineString; }
}
