package TurnQuest.view.Base;

public class MimeTypes {

    public static String getMimeType(String fileName) {

        String mime = null;

        String ext = fileName.toLowerCase();
        if (ext.endsWith(".pdf")) {
            mime = "application/pdf";
        } else if (ext.endsWith(".doc")) {
            mime = "application/msword";
        } else if (ext.endsWith(".ppt")) {
            mime = "application/vnd.ms-powerpoint";
        } else if (ext.endsWith(".rar")) {
            mime = "application/octet-stream";
        } else if (ext.endsWith(".zip")) {
            mime = "application/zip";
        } else if (ext.endsWith(".jpg")) {
            mime = "image/jpeg";
        } else if (ext.endsWith(".jpeg")) {
            mime = "image/jpeg";
        } else if (ext.endsWith(".gif")) {
            mime = "image/gif";
        } else if (ext.endsWith(".png")) {
            mime = "image/png";
        }
        return mime;
    }
}
