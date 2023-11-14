package TurnQuest.view.Base;


import TurnQuest.view.Connect.DBConnector;

import java.io.IOException;
import java.io.InputStream;

import java.math.BigDecimal;

import java.sql.CallableStatement;
import java.sql.Connection;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import javax.servlet.http.HttpSession;

import oracle.adf.view.rich.component.rich.input.RichInputFile;
import oracle.adf.view.rich.component.rich.output.RichImage;

import org.apache.myfaces.trinidad.model.UploadedFile;


public class ImageUploadBean {
    private RichImage clientImage;
    private RichInputFile uploadComponent;

    public ImageUploadBean() {
        super();
    }
    HttpSession session =
        (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);

    private UploadedFile UploadedImageFile;

    public void setUploadedImageFile(UploadedFile UploadedImageFile) {
        this.UploadedImageFile = UploadedImageFile;

        InputStream Reader;
        try {
            long Val = UploadedImageFile.getLength();
            Reader = UploadedImageFile.getInputStream();
            byte[] ImageBytes = new byte[Reader.available()];
            //int BytesLength = ImageBytes.length;

            InsertClientImage(Reader, Val);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UploadedFile getUploadedImageFile() {
        return UploadedImageFile;
    }

    public void ImageUploadedListener(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            UploadedFile _file = (UploadedFile)valueChangeEvent.getNewValue();
            InputStream Reader;
            this.UploadedImageFile = _file;
            try {
                long Val = UploadedImageFile.getLength();
                Reader = UploadedImageFile.getInputStream();
                byte[] ImageBytes = new byte[Reader.available()];
                //int BytesLength = ImageBytes.length;

                InsertClientImage(Reader, Val);
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(e);
            }
        }
    }

    public void ImageOrgGroupUploadedListener(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        if (valueChangeEvent.getNewValue() != valueChangeEvent.getOldValue()) {
            UploadedFile _file = (UploadedFile)valueChangeEvent.getNewValue();
            InputStream Reader;
            this.UploadedImageFile = _file;
            try {
                long Val = UploadedImageFile.getLength();
                Reader = UploadedImageFile.getInputStream();
                byte[] ImageBytes = new byte[Reader.available()];
                //int BytesLength = ImageBytes.length;

                InsertGroupImage(Reader, Val);
            } catch (Exception e) {
                GlobalCC.EXCEPTIONREPORTING(e);
            }
        }

    }

    public void setClientImage(RichImage clientImage) {
        this.clientImage = clientImage;
    }

    public RichImage getClientImage() {
        return clientImage;
    }

    public void InsertClientImage(InputStream Image, long BytesLength) {
        try {
            DBConnector connector = new DBConnector();
            Connection conn = connector.getDatabaseConnection();
            String systemsQuery =
                "BEGIN TQC_WEB_ORGANIZATION_PKG.update_image(?,?,?);END;";
            CallableStatement cst = null;
            cst = conn.prepareCall(systemsQuery);
            cst.setBlob(1, Image, BytesLength);
            cst.setBigDecimal(2, (BigDecimal)session.getAttribute("ORGCode"));
            cst.setBigDecimal(3, new BigDecimal(1));
            cst.execute();

            //ADFUtils.findIterator("findAllQuotationDetailsIterator").executeQuery();
            // GlobalCC.refreshUI(clientImage);

            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
    }

    public void InsertGroupImage(InputStream Image, long BytesLength) {
        try {
            DBConnector connector = new DBConnector();
            Connection conn = connector.getDatabaseConnection();
            String systemsQuery =
                "BEGIN TQC_WEB_ORGANIZATION_PKG.update_image(?,?,?);END;";
            CallableStatement cst = null;
            cst = conn.prepareCall(systemsQuery);
            cst.setBlob(1, Image, BytesLength);
            cst.setBigDecimal(2, (BigDecimal)session.getAttribute("ORGCode"));
            cst.setBigDecimal(3, new BigDecimal(2));
            cst.execute();

            //ADFUtils.findIterator("findAllQuotationDetailsIterator").executeQuery();
            // GlobalCC.refreshUI(clientImage);

            conn.close();
        } catch (Exception e) {
            GlobalCC.EXCEPTIONREPORTING(e);
        }
    }

    public void setUploadComponent(RichInputFile uploadComponent) {
        this.uploadComponent = uploadComponent;
    }

    public RichInputFile getUploadComponent() {
        return uploadComponent;
    }
}


