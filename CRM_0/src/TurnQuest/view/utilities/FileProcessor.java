package TurnQuest.view.utilities;

import java.io.IOException;

import org.apache.myfaces.trinidad.model.UploadedFile;


public class FileProcessor {

    private CSVtoADFTableProcessor tablecreator;
    private UploadedFile uploadedFile;
    private String filename;
    private long filesize;
    private String filecontents;
    private String filetype;


    public FileProcessor() {
    }

    public String myProcedure() {
        UploadedFile uploadedFile = this.uploadedFile;
        setUploadedFile(uploadedFile);
        return null;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
        this.filename = uploadedFile.getFilename();
        this.filesize = uploadedFile.getLength();
        this.filetype = uploadedFile.getContentType();
        try {
            System.out.println("Filename");
            System.out.println(uploadedFile.getFilename());
            System.out.println("Length");
            System.out.println(uploadedFile.getLength());
            System.out.println("Content");
            System.out.println(uploadedFile.getContentType());
            CSVtoADFTableProcessor dsd = new CSVtoADFTableProcessor();
            dsd.processCSV(uploadedFile.getInputStream());
            // tablecreator.processCSV(uploadedFile.getInputStream());
        } catch (IOException e) {
            // TODO
        }
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilesize(long filesize) {
        this.filesize = filesize;
    }

    public long getFilesize() {
        return filesize;
    }

    public void setFilecontents(String filecontents) {
        this.filecontents = filecontents;
    }

    public String getFilecontents() {
        return filecontents;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setTablecreator(CSVtoADFTableProcessor tablecreator) {
        this.tablecreator = tablecreator;
    }

    public CSVtoADFTableProcessor getTablecreator() {
        return tablecreator;
    }
}
