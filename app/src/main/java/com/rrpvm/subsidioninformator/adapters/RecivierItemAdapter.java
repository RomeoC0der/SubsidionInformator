package com.rrpvm.subsidioninformator.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rrpvm.subsidioninformator.R;
import com.rrpvm.subsidioninformator.fragments.RecivierDialogInformation;
import com.rrpvm.subsidioninformator.objects.SubsidingRecivier;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class RecivierItemAdapter extends ArrayAdapter<SubsidingRecivier> {
    private LayoutInflater inflater;
    private int layout;
    private ArrayList<SubsidingRecivier> data;
    public RecivierItemAdapter(Context ctx, int resource, ArrayList<SubsidingRecivier> reciviers) {
        super(ctx, resource, reciviers);
        this.data = reciviers;
        this.layout = resource;
        this.inflater = LayoutInflater.from(ctx);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(this.layout, parent, false);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        ImageView recivierIconView = (ImageView) convertView.findViewById(R.id.recivier_logotype);
        TextView nameView = (TextView) convertView.findViewById(R.id.receiving_pib);
        TextView regionView = (TextView) convertView.findViewById(R.id.receiving_region);
        TextView positionView = (TextView) convertView.findViewById(R.id.recivier_position);
        TextView birthdateView = (TextView) convertView.findViewById(R.id.recivier_birthdate);
        Button moreDetailsButton = (Button) convertView.findViewById(R.id.recivier_more_button);
        final SubsidingRecivier currentReciever = data.get(position);
        nameView.setText(currentReciever.getPIB());
        regionView.setText(convertView.getResources().getText(R.string.nav_menu_region_hint).toString() + ": " + currentReciever.getRegion());
        positionView.setText(convertView.getResources().getText(R.string.nav_menu_city_hint).toString() + ": " + currentReciever.getCity());
        birthdateView.setText(convertView.getResources().getText(R.string.nav_menu_dateHint).toString() + ": " + dateFormat.format(currentReciever.getBirthdate()).toString());
        int imgId = recivierIconView.getContext().getResources().getIdentifier(currentReciever.getImage(), "drawable", recivierIconView.getContext().getPackageName());
        if (imgId != 0)
            recivierIconView.setImageResource(imgId);
        else
            recivierIconView.setImageResource(currentReciever.isMale() ? R.drawable.default_man_icon_foreground : R.drawable.default_women_icon_foreground);
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


        return convertView;
    }
}
