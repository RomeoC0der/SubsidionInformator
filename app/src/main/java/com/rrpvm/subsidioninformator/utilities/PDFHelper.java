package com.rrpvm.subsidioninformator.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.rrpvm.subsidioninformator.R;
import com.rrpvm.subsidioninformator.objects.BitmapWrapper;
import com.rrpvm.subsidioninformator.objects.SubsidingRecivier;

import java.io.File;

public class PDFHelper {
    public static String FONT_NAME = "res/font/montserrat.ttf";
    public static String FONT_CHIVO_NAME = "res/font/chivo.ttf";
    public static Context context;
    private static final int TXT_START = 70;
    private static final int TXT_MARGIN = 20;

    public static void initContext(Context _context) {
        context = _context;
    }

    public static boolean export(SubsidingRecivier subsidingRecivier) {
        final String filePath = subsidingRecivier.getPIB() + ".pdf";
        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "");
        if (!directory.exists()) {
            directory.mkdirs();//create directory with parents
        }
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), filePath);
        try {
            PdfDocument pdfDocument = new PdfDocument(new PdfWriter(file));//mode:creation
            Document document = new Document(pdfDocument, PageSize.A6);
            PdfPage mainPage = pdfDocument.addNewPage();
            PdfDocumentInfo information = document.getPdfDocument().getDocumentInfo();//содержит информацию о документе
            PdfFont font = PdfFontFactory.createFont(FONT_NAME, PdfEncodings.IDENTITY_H);
            document.setFont(font);
            PdfCanvas canvas = new PdfCanvas(mainPage);
            Rectangle pageSize = document.getPdfDocument().getDefaultPageSize();
            canvas.setFillColor(new DeviceRgb(55, 55, 81));//header color
            canvas.rectangle(0, pageSize.getHeight() - 36, pageSize.getWidth(), 36).fill();

            PDFParagraph headerTitle = new PDFParagraph(context.getString(R.string.company_name), 15, 26, pageSize, new DeviceRgb(255, 255, 255), 8);
            PDFParagraph subTitle = new PDFParagraph("SUBSIDII@IOC.GOV.UA", (int) pageSize.getWidth() - 75, 36, pageSize, new DeviceRgb(255, 255, 255), 6);
            PDFImage avatar = new PDFImage(subsidingRecivier.getImage(), 105, 105);
            avatar.setPosition(15, 50, pageSize);
            PDFParagraph pFIO = new PDFParagraph(subsidingRecivier.getPIB(), 130, TXT_START, pageSize, new DeviceRgb(0, 0, 0), 10);
            PDFParagraph pPID = new PDFParagraph("passport ID: ".concat(subsidingRecivier.getPassportId()), 130, TXT_START + TXT_MARGIN, pageSize, new DeviceRgb(0, 0, 0), 8);
            PDFParagraph pTIN = new PDFParagraph("TIN ID: ".concat(subsidingRecivier.getITN()), 130, TXT_START + TXT_MARGIN * 2, pageSize, new DeviceRgb(0, 0, 0), 8);
            PDFParagraph pLOCATION = new PDFParagraph(String.format("geolocation: %s oblast/%s/%s", subsidingRecivier.getRegion(), subsidingRecivier.getCity(), subsidingRecivier.getPosition()), 130, TXT_START + TXT_MARGIN * 3, pageSize, new DeviceRgb(0, 0, 0), 6);

            PDFParagraph pSeparator = new PDFParagraph("Subsidion Information", 0, 185, pageSize, new DeviceRgb(0, 0, 0), 12,true);

            PDFParagraph pSubsID = new PDFParagraph("Subsidion ID: ".concat(Integer.toString(subsidingRecivier.getSubsidionData().getId())), 15, 215, pageSize, new DeviceRgb(0, 0, 0), 10,false);
            PDFParagraph pSubStatement = new PDFParagraph("Subsidion Statement: ".concat(subsidingRecivier.getSubsidionData().getStatement()? "true":"false"), 15, 215+TXT_MARGIN, pageSize, new DeviceRgb(0, 0, 0), 10,false);
            PDFParagraph pSubSize = new PDFParagraph("Subsidion size: ".concat(Double.toString(subsidingRecivier.getSubsidionData().getCGTP())).concat(" UAH"), 15, 215+TXT_MARGIN*2, pageSize, new DeviceRgb(0, 0, 0), 10,false);
            PDFParagraph pSubRangeA = new PDFParagraph("Subsidion date arrived: ".concat(subsidingRecivier.getSubsidionData().getRecievRange()), 15, 215+TXT_MARGIN*3, pageSize, new DeviceRgb(0, 0, 0), 10,false);
            PDFParagraph pSubRangeG = new PDFParagraph("Subsidion date Taken: ".concat(subsidingRecivier.getSubsidionData().getGotRange()), 15, 215+TXT_MARGIN*4, pageSize, new DeviceRgb(0, 0, 0), 10,false);

            document.add(headerTitle.getParagraph());
            document.add(subTitle.getParagraph());
            document.add(avatar.getImage());
            document.add(pFIO.getParagraph());
            document.add(pPID.getParagraph());
            document.add(pTIN.getParagraph());
            document.add(pLOCATION.getParagraph());
            document.add(pSeparator.getParagraph());
            document.add(pSubsID.getParagraph());
            document.add(pSubStatement.getParagraph());
            document.add(pSubSize.getParagraph());
            document.add(pSubRangeA.getParagraph());
            document.add(pSubRangeG.getParagraph());


            document.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

class PDFParagraph {
    private Paragraph paragraph = null;
    private PdfFont font = null;
    private Color fontColor = new DeviceRgb(0, 0, 0);
    private int fontSize = 12;
    private String value = new String();
    private boolean isCentered = false;
    private int x = 0;
    private int y = 0;

    public PDFParagraph(Paragraph paragraph) {
        this.paragraph = paragraph;
    }

    public PDFParagraph(String value) {
        this.value = value;
        this.paragraph = new Paragraph(value);
    }

    public PDFParagraph(String value, int x, int y, Rectangle pageSize) {
        this(value);
        this.x = x;
        this.y = y;
        this.paragraph.setFixedPosition(x, pageSize.getHeight() - y, pageSize.getWidth());
    }

    public PDFParagraph(String value, int x, int y, Rectangle pageSize, Color fontColor) {
        this(value, x, y, pageSize);
        this.paragraph.setFontColor(fontColor);
        this.fontColor = fontColor;
    }

    public PDFParagraph(String value, int x, int y, Rectangle pageSize, Color fontColor, int fontSize) {
        this(value, x, y, pageSize, fontColor);
        this.paragraph.setFontSize(fontSize);
        this.fontSize = fontSize;
    }
    public PDFParagraph(String value, int x, int y, Rectangle pageSize, Color fontColor, int fontSize,boolean isCentered) {
        this(value, x, y, pageSize, fontColor);
        this.paragraph.setFontSize(fontSize);
        this.fontSize = fontSize;
        this.isCentered = true;
        if(isCentered)this.paragraph.setTextAlignment(TextAlignment.CENTER);
    }
    public PDFParagraph(String value, int x, int y, Rectangle pageSize, Color fontColor, PdfFont font, int fontSize) {
        this(value, x, y, pageSize, fontColor);
        this.paragraph.setFontSize(fontSize);
        this.paragraph.setFont(font);
        this.fontSize = fontSize;
        this.font = font;
    }

    public Paragraph getParagraph() {
        return this.paragraph;
    }

    public void setParagraph(Paragraph p) {
        this.paragraph = p;
    }
    //public void setPosition(int x, int y,Rectangle pageSize){
    // this.paragraph.setFixedPosition(x, (int)(pageSize.getHeight()-y)),paragraph.getWidth());
    //}
}

class PDFImage {
    private Image image;
    private int heigth;

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public PDFImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        ImageData imageData = ImageDataFactory.create(byteArrayOutputStream.toByteArray());
        this.image = new Image(imageData);
        this.heigth = (int) this.image.getImageHeight();
    }

    public PDFImage(BitmapWrapper bitmapWrapper) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmapWrapper.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        ImageData imageData = ImageDataFactory.create(byteArrayOutputStream.toByteArray());
        this.image = new Image(imageData);
    }

    public PDFImage(BitmapWrapper bitmapWrapper, int w, int h) {
        BitmapWrapper copy = new BitmapWrapper(bitmapWrapper);
        copy.scaleTo(w, h);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        copy.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        ImageData imageData = ImageDataFactory.create(byteArrayOutputStream.toByteArray());
        this.image = new Image(imageData);
        this.heigth = h;

    }

    public PDFImage(BitmapWrapper bitmapWrapper, int maxHeigth) {
        BitmapWrapper wrapperCopy = new BitmapWrapper(bitmapWrapper);
        wrapperCopy.scaleWithAspectRatio(maxHeigth);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        wrapperCopy.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        ImageData imageData = ImageDataFactory.create(byteArrayOutputStream.toByteArray());
        this.image = new Image(imageData);
        this.heigth = maxHeigth;
    }

    public void setPosition(int x, int y, Rectangle pageSize) {
        image.setFixedPosition(x, pageSize.getHeight() - y - this.heigth);
    }
}