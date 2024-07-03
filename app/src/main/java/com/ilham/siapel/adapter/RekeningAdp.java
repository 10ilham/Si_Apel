package com.ilham.siapel.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ilham.siapel.R;
import com.ilham.siapel.model.ModelRekening;

import java.util.List;

public class RekeningAdp extends BaseAdapter {
    Activity activity;
    LayoutInflater inflater;
    List<ModelRekening> rekenings;

    public RekeningAdp(Activity activity, List<ModelRekening> rekenings) {
        this.activity = activity;
        this.rekenings = rekenings;
    }

    @Override
    public int getCount() {
        return rekenings.size();
    }

    @Override
    public Object getItem(int position) {
        return rekenings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null){
            inflater=(LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null && inflater!=null){
            convertView=inflater.inflate(R.layout.list_rekening, null);
        }
        if (convertView!=null){
            TextView tvIdRekening=convertView.findViewById(R.id.tv_id_rekening);
            TextView tvNamaRekening=convertView.findViewById(R.id.tv_nama_rekening);

            ModelRekening rekening=rekenings.get(position);
            tvIdRekening.setText(rekening.getId_rek());
            tvNamaRekening.setText(rekening.getNama_rek());
        }
        return convertView;
    }
}
