package TurnQuest.view.Base;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.sql.SQLException;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.servlet.http.HttpServletResponse;

import oracle.jbo.domain.BlobDomain;

import org.apache.myfaces.trinidad.model.UploadedFile;


public class FileUpload {

    public static synchronized BlobDomain writeToBlobDomain(UploadedFile file) throws SQLException,
                                                                                      IOException {

        InputStream in = file.getInputStream();
        BlobDomain blobDomain = new BlobDomain();
        OutputStream out = blobDomain.getBinaryOutputStream();

        byte[] buffer = new byte[8192];
        int bytesRead = 0;
        while ((bytesRead = in.read(buffer, 0, 8192)) != -1) {
            out.write(buffer, 0, bytesRead);
        }
        in.close();

        return blobDomain;
    }

    public static synchronized void downloadFile(String fileName,
                                                 BlobDomain blobDomain) {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext extContext = facesContext.getExternalContext();

        Long length = blobDomain.getLength();
        String fileType = MimeTypes.getMimeType(fileName);

        HttpServletResponse response =
            (HttpServletResponse)extContext.getResponse();
        response.setHeader("Content-Disposition",
                           "attachment;filename=\"" + fileName + "\"");
        response.setContentLength(length.intValue());

        response.setContentType(fileType);
        try {
            InputStream in = blobDomain.getBinaryStream();
            OutputStream out = response.getOutputStream();

            byte[] buf = new byte[1024];
            int count;
            while ((count = in.read(buf)) >= 0) {
                out.write(buf, 0, count);
            }

            in.close();
            out.flush();
            out.close();
            facesContext.responseComplete();

        } catch (IOException ex) {

            ex.printStackTrace();
        }
    }
}
