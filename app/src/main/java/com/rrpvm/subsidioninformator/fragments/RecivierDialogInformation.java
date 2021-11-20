package com.rrpvm.subsidioninformator.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.io.font.PdfEncodings;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Paragraph;

import com.itextpdf.layout.property.TextAlignment;
import com.rrpvm.subsidioninformator.R;
import com.rrpvm.subsidioninformator.objects.SubsidingRecivier;

import java.io.File;

//todo: permission implementation
public class RecivierDialogInformation extends DialogFragment {
    public static String FONT_NAME = "res/font/freesans.ttf";
    private Context ctx;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    public Dialog onCreateDialog(Bundle saveInstanceState) {
        final SubsidingRecivier recivier = (SubsidingRecivier) getArguments().getSerializable("recivier_data");
        if (recivier == null) return null;//todo: try + catch
        this.ctx = getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View recivierLayout = generateLayoutData(inflater.inflate(R.layout.recivier_info_dialog, null), recivier);
        builder.setView(recivierLayout);
        return builder.setTitle("Дані").
                setNeutralButton(getResources().getText(R.string.dialog_string_button_exportPDF), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        exportToPDF(recivier);
                    }
                }).setPositiveButton(getResources().getText(R.string.dialog_string_button_closeDialog), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create();
    }
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
    public View generateLayoutData(View layout, SubsidingRecivier subsidingRecivier) {
        ImageView logo = layout.findViewById(R.id.dialog_data_image);
        TextView pibView = layout.findViewById(R.id.dialog_data_pib);
        TextView tinView = layout.findViewById(R.id.dialog_data_TIN);
        TextView positionView = layout.findViewById(R.id.dialog_data_position);
        TextView subsudionStatement = layout.findViewById(R.id.dialog_data_subsidion_statement);
        TextView passIdView = layout.findViewById(R.id.dialog_data_pass_id);
        TextView subsidionIdView = layout.findViewById(R.id.dialog_data_subsidion_id);
        TextView monthSubsidionView = layout.findViewById(R.id.dialog_data_month_subs_size);
        TextView yearSubsidionView = layout.findViewById(R.id.dialog_data_year_subs_size);
        TextView arrivedSubsidionView = layout.findViewById(R.id.dialog_data_arrived_diaposon);
        TextView getSubsidionView = layout.findViewById(R.id.dialog_data_get_diaposon);
        pibView.setText(subsidingRecivier.getPIB());
        tinView.setText(this.getResources().getText(R.string.dialog_string_tin_number) + ": " + subsidingRecivier.getITN());
        passIdView.setText(this.getResources().getText(R.string.dialog_string_passportId) + ": " + subsidingRecivier.getPassportId());
        positionView.setText(this.getResources().getText(R.string.dialog_string_geopos) + ": " + subsidingRecivier.getCity());
        String subsStatementString = subsidingRecivier.getSubsidionData().getStatement() ? "діє" : "недіюча";//можно не выделять переменную
        subsudionStatement.setText(this.getResources().getText(R.string.dialog_string_subsidion_statement) + ": " + subsStatementString);
        subsidionIdView.setText(this.getResources().getText(R.string.dialog_string_subsidion_id) + ": " + subsidingRecivier.getSubsidionData().getId());
        monthSubsidionView.setText(this.getResources().getText(R.string.dialog_string_month_subs_size) + ": " + subsidingRecivier.getSubsidionData().getJKP());
        yearSubsidionView.setText(this.getResources().getText(R.string.dialog_string_year_subs_size) + ": " + subsidingRecivier.getSubsidionData().getCGTP());
        arrivedSubsidionView.setText(this.getResources().getText(R.string.dialog_string_arrived_diaposon) + ": " + subsidingRecivier.getSubsidionData().getRecievRange());
        getSubsidionView.setText(this.getResources().getText(R.string.dialog_string_get_diaposon) + ": " + subsidingRecivier.getSubsidionData().getGotRange());
        int imgId = this.getContext().getResources().getIdentifier(subsidingRecivier.getImage(), "drawable", this.getContext().getPackageName());
        if (imgId != 0)
            logo.setImageResource(imgId);
        else
            logo.setImageResource(subsidingRecivier.isMale() ? R.drawable.default_man_icon_foreground : R.drawable.default_women_icon_foreground);
        return layout;
    }
    void exportToPDF(SubsidingRecivier subsidingRecivier) {//todo:доделать
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
            Toast.makeText(ctx, "документ збережено за адресою: " + directory.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}