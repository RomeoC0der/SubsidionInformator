package com.rrpvm.subsidioninformator.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rrpvm.subsidioninformator.R;
import com.rrpvm.subsidioninformator.activities.EditRecivierDataActivity;
import com.rrpvm.subsidioninformator.fragments.RecivierDialogInformation;
import com.rrpvm.subsidioninformator.handlers.AuthorizationHandler;
import com.rrpvm.subsidioninformator.interfaces.Redirectable;
import com.rrpvm.subsidioninformator.objects.SubsidingRecivier;
import com.rrpvm.subsidioninformator.objects.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class RecivierItemAdapter extends ArrayAdapter<SubsidingRecivier> implements Redirectable {
    public RecivierItemAdapter(Context ctx, int resource, ArrayList<SubsidingRecivier> reciviers) {
        super(ctx, resource, reciviers);
        this.data = reciviers;
        this.layout = resource;
        this.inflater = LayoutInflater.from(ctx);
    }

    public void bindContext(Context context) {
        this.mainActivityContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(this.layout, parent, false);
        }
        final SubsidingRecivier currentReciever = data.get(position);
        //<get all views>
        ImageView recivierIconView = (ImageView) convertView.findViewById(R.id.recivier_logotype);
        TextView nameView = (TextView) convertView.findViewById(R.id.receiving_pib);
        TextView regionView = (TextView) convertView.findViewById(R.id.receiving_region);
        TextView positionView = (TextView) convertView.findViewById(R.id.recivier_position);
        TextView birthdateView = (TextView) convertView.findViewById(R.id.recivier_birthdate);
        Button moreDetailsButton = (Button) convertView.findViewById(R.id.recivier_more_button);
        Button editDetailsButton = (Button) convertView.findViewById(R.id.recivier_edit_button);

        //PERMISSIONS:
        if (AuthorizationHandler.getInstance().getUserSession().getUserType() == User.UserType.C_USER)
            editDetailsButton.setEnabled(false);

        //<data manipulations>
        nameView.setText(currentReciever.getPIB());
        regionView.setText(convertView.getResources().getString(R.string.datable_region_string, currentReciever.getRegion()));
        positionView.setText(convertView.getResources().getString(R.string.datable_city_string, currentReciever.getCity()));
        birthdateView.setText(convertView.getResources().getString(R.string.datable_birthdate_string, dateFormat.format(currentReciever.getBirthdate())));
        Bitmap imgToPresent = null;
        try {
            imgToPresent = currentReciever.getImage().getBitmap().get();
            recivierIconView.setImageBitmap(imgToPresent);
        } catch (Exception e) {
            e.printStackTrace();
            recivierIconView.setImageResource(currentReciever.isMale() ? R.drawable.default_man_icon_foreground : R.drawable.default_women_icon_foreground);//default icon if we cant find normal image
        }
        moreDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecivierDialogInformation infoDialogFragment = new RecivierDialogInformation();
                Bundle bundle = new Bundle();
                bundle.putSerializable("recivier_data", currentReciever);//в поток
                infoDialogFragment.setArguments(bundle);//передаем фрагменту этот поток данных
                infoDialogFragment.show(((Activity) getContext()).getFragmentManager(), "custom");
            }
        });
        editDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toSerializeRecivier = currentReciever;
                redirect();
            }
        });
        return convertView;
    }

    @Override
    public void redirect() {
        Bundle bundle = new Bundle();
        Intent message = new Intent(mainActivityContext, EditRecivierDataActivity.class);
        message.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        bundle.putSerializable("recivier_data", toSerializeRecivier);//в поток
        bundle.putInt(EditRecivierDataActivity.bundleArgumentMode, EditRecivierDataActivity.EDIT_MODE.EDIT_EXIST_USER.getValue());//в поток
        message.putExtras(bundle);
        mainActivityContext.startActivity(message);
    }

    private LayoutInflater inflater;
    private int layout;
    private ArrayList<SubsidingRecivier> data;
    private Context mainActivityContext;
    private SubsidingRecivier toSerializeRecivier;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
}
