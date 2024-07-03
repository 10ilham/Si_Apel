package com.ilham.siapel.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ilham.siapel.R;
import com.ilham.siapel.model.ModelPelanggan;

import java.util.List;

public class PelangganAdp extends BaseAdapter {
    Activity activity;
    LayoutInflater inflater;
    List<ModelPelanggan> pelanggans;

    public PelangganAdp(Activity activity, List<ModelPelanggan> pelanggans) {
        this.activity = activity;
        this.pelanggans = pelanggans;
    }

    @Override
    public int getCount() {
        return pelanggans.size();
    }

    @Override
    public Object getItem(int position) {
        return pelanggans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null && inflater != null) {
            convertView = inflater.inflate(R.layout.list_pelanggan, null); //Memanggil layout dari CardView di list_pelanggan.xml
        }
        if (convertView!=null){
            TextView tvId = convertView.findViewById(R.id.tv_id);
            TextView tvNama= convertView.findViewById(R.id.tv_nama);
            TextView tvJenis = convertView.findViewById(R.id.tv_jenis);

            ModelPelanggan pelanggan = pelanggans.get(position);
            tvId.setText(pelanggan.getId_pel());
            tvNama.setText(pelanggan.getNama_pel());
            tvJenis.setText(pelanggan.getJenis_rek());
        }
        return convertView;
    }
}