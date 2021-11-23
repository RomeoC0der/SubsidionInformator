package com.rrpvm.subsidioninformator.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rrpvm.subsidioninformator.R;
import com.rrpvm.subsidioninformator.objects.SubsidingRecivier;
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
        return builder.setTitle("Дані").
                setNeutralButton(getResources().getText(R.string.dialog_confirm_export_pdf), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (PDFHelper.export(recivier))
                            Toast.makeText(ctx, "документ збережено в Documents", Toast.LENGTH_SHORT).show();
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
        /*Get all vies*/
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
        pibView.setText(subsidingRecivier.getPIB());
        tinView.setText(this.getResources().getString(R.string.element_tin_number, subsidingRecivier.getITN()));
        passIdView.setText(this.getResources().getString(R.string.element_passport_ID, subsidingRecivier.getPassportId()));
        positionView.setText(this.getResources().getString(R.string.element_geodata, subsidingRecivier.getCity()));
        subsudionStatement.setText(this.getResources().getString(R.string.element_subsidion_statement, getResources().getText(subsidingRecivier.getSubsidionData().getStatement() ? R.string.element_subsidion_statement_true : R.string.element_subsidion_statement_false)));
        subsidionIdView.setText(this.getResources().getString(R.string.element_subsidion_ID, subsidingRecivier.getSubsidionData().getId()));
        monthSubsidionView.setText(this.getResources().getString(R.string.element_month_subsidion_size, subsidingRecivier.getSubsidionData().getJKP()));
        yearSubsidionView.setText(this.getResources().getString(R.string.element_year_subsidion_size, subsidingRecivier.getSubsidionData().getCGTP()));
        arrivedSubsidionView.setText(this.getResources().getString(R.string.element_arrived_diaposon, subsidingRecivier.getSubsidionData().getRecievRange()));
        getSubsidionView.setText(this.getResources().getString(R.string.element_taken_diaposon, subsidingRecivier.getSubsidionData().getGotRange()));
        int imgId = this.getContext().getResources().getIdentifier(subsidingRecivier.getImage(), "drawable", this.getContext().getPackageName());
        if (imgId != 0)
            logo.setImageResource(imgId);
        else
            logo.setImageResource(subsidingRecivier.isMale() ? R.drawable.default_man_icon_foreground : R.drawable.default_women_icon_foreground);
        return layout;
    }
}