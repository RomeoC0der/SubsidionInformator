package com.rrpvm.subsidioninformator.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import android.app.DialogFragment;



public class EditElementDialog extends DialogFragment {
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
    @Override
    public Dialog onCreateDialog(Bundle saveInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        return builder.setTitle("Повiдомлення").setMessage("Будь ласка, заповніть усі поля").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).create();
    }
}
