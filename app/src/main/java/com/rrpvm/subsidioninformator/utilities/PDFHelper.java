package com.rrpvm.subsidioninformator.utilities;

import android.os.Environment;
import android.widget.Toast;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.rrpvm.subsidioninformator.objects.SubsidingRecivier;

import java.io.File;

public class PDFHelper {
    public static String FONT_NAME = "res/font/freesans.ttf";

    public static boolean export(SubsidingRecivier subsidingRecivier) {
        final String filePath = subsidingRecivier.getPIB() + ".pdf";
        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "");
        if (!directory.exists()) {
            directory.mkdirs();//create directory with parents
        }
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), filePath);
        try {
            PdfFont font = PdfFontFactory.createFont(FONT_NAME, PdfEncodings.IDENTITY_H);
            PdfDocument pdfDocument = new PdfDocument(new PdfWriter(file));//mode:creation
            Document document = new Document(pdfDocument, PageSize.A6);
            document.setMargins(32, 32, 32, 32);
            PdfDocumentInfo information = document.getPdfDocument().getDocumentInfo();//содержит информацию о документе
            document.setFont(font);
            document.setFontSize(10);
            document.add(new Paragraph("ПIБ отримувача: " + subsidingRecivier.getPIB()));
            document.add(new Paragraph("IНП: " + subsidingRecivier.getPassportId()));
            document.add(new Paragraph("Номер паспорту: " + subsidingRecivier.getITN()));
            document.add(new Paragraph("Місце прописки: " + subsidingRecivier.getRegion() + " " + subsidingRecivier.getCity() + " " + subsidingRecivier.getPosition()));
            document.add(new Paragraph("Cтан субсидии: " + subsidingRecivier.getSubsidionData().getStatement()));
            document.add(new Paragraph("Номер заяви: " + subsidingRecivier.getSubsidionData().getId()));
            document.add(new Paragraph("Розмір Субсидії ЖКП За Місяць, грн: " + subsidingRecivier.getSubsidionData().getJKP()));
            document.add(new Paragraph("Розмір Субсидії СГТП Річний, грн: " + subsidingRecivier.getSubsidionData().getCGTP()));
            document.add(new Paragraph("Призначено За Період: " + subsidingRecivier.getSubsidionData().getRecievRange()));
            document.add(new Paragraph("Нараховано За Період: " + subsidingRecivier.getSubsidionData().getGotRange()));
            document.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
