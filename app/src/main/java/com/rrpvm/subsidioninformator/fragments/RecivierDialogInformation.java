package com.rrpvm.subsidioninformator.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.rrpvm.subsidioninformator.R;
import com.rrpvm.subsidioninformator.handlers.AuthorizationHandler;
import com.rrpvm.subsidioninformator.objects.SubsidingRecivier;
import com.rrpvm.subsidioninformator.objects.User;
import com.rrpvm.subsidioninformator.utilities.PDFHelper;


//todo: permission implementation
public class RecivierDialogInformation extends DialogFragment {
    private Context ctx = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ctx = context;
    }

    public Dialog onCreateDialog(Bundle saveInstanceState) {
        final SubsidingRecivier recivier = (SubsidingRecivier) getArguments().getSerializable("recivier_data");
        if (recivier == null) return null;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View recivierLayout = generateLayoutData(inflater.inflate(R.layout.recivier_info_dialog, null), recivier);
        builder.setView(recivierLayout);
        return builder.setTitle(getResources().getText(R.string.title_dialog_more_information)).
                setNeutralButton(getResources().getText(R.string.dialog_confirm_export_pdf), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (PDFHelper.export(recivier))
                            Toast.makeText(ctx, getText(R.string.toast_document_saved), Toast.LENGTH_SHORT).show();
                    }
                }).setPositiveButton(getResources().getText(R.string.dialog_confirm_close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).create();
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public View generateLayoutData(View layout, SubsidingRecivier subsidingRecivier) {
        /*Get all views*/
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
        /*Manipulate with data*/

        //permission check:
        AuthorizationHandler authorizationHandler = AuthorizationHandler.getInstance();
        int user_permission = authorizationHandler.getUserSession().getUserType().getId();
        pibView.setText(subsidingRecivier.getPIB());
        tinView.setText(this.getResources().getString(R.string.element_tin_number, subsidingRecivier.getITN()));
        passIdView.setText(this.getResources().getString(R.string.element_passport_ID, subsidingRecivier.getPassportId()));
        subsidionIdView.setText(this.getResources().getString(R.string.element_subsidion_ID, subsidingRecivier.getSubsidionData().getId()));
        subsudionStatement.setText(this.getResources().getString(R.string.element_subsidion_statement, getResources().getText(subsidingRecivier.getSubsidionData().getStatement() ? R.string.element_subsidion_statement_true : R.string.element_subsidion_statement_false)));
        positionView.setText(this.getResources().getString(R.string.element_geodata, String.format("%s/%s/%s", subsidingRecivier.getRegion(), subsidingRecivier.getCity(), subsidingRecivier.getPosition())));
        //  setInputTextWithPermission(positionView, getString(R.string.element_geodata, String.format("%s/%s/%s", subsidingRecivier.getRegion(), subsidingRecivier.getCity(), subsidingRecivier.getPosition())), user_permission);
        //  setInputTextWithPermission(subsidionIdView, getString(R.string.element_subsidion_ID, subsidingRecivier.getSubsidionData().getId()), user_permission);
        //  setInputTextWithPermission(subsudionStatement, getString(R.string.element_subsidion_statement, getResources().getText(subsidingRecivier.getSubsidionData().getStatement() ? R.string.element_subsidion_statement_true : R.string.element_subsidion_statement_false)), user_permission);
        monthSubsidionView.setText(this.getResources().getString(R.string.element_month_subsidion_size, subsidingRecivier.getSubsidionData().getJKP()));
        yearSubsidionView.setText(this.getResources().getString(R.string.element_year_subsidion_size, subsidingRecivier.getSubsidionData().getCGTP()));
        arrivedSubsidionView.setText(this.getResources().getString(R.string.element_arrived_diaposon, subsidingRecivier.getSubsidionData().getRecievRange()));
        getSubsidionView.setText(this.getResources().getString(R.string.element_taken_diaposon, subsidingRecivier.getSubsidionData().getGotRange()));
        try {
            Bitmap imgToPresent = subsidingRecivier.getImage().getBitmap().get();
            logo.setImageBitmap(imgToPresent);
        } catch (Exception e) {
            e.printStackTrace();
            logo.setImageResource(subsidingRecivier.isMale() ? R.drawable.default_man_icon_foreground : R.drawable.default_women_icon_foreground);//default icon if we cant find normal image
        }
        return layout;
    }

    /*private void setInputTextWithPermission(TextView layout, String value, int permisson) {
        if (permisson == User.UserType.C_USER.getId()) {
            layout.setText("you haven't enough permissions");
        } else layout.setText(value);
    }*/
}