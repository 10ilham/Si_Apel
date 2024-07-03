package com.ilham.siapel.aktiviti;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ilham.siapel.R;
import com.ilham.siapel.adapter.PelangganAdp;
import com.ilham.siapel.helper.HelperDb;
import com.ilham.siapel.model.ModelPelanggan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PelangganList extends AppCompatActivity {

    ListView listView;
    AlertDialog.Builder dialog;
    List<ModelPelanggan> pelanggans=new ArrayList<>();
    PelangganAdp pelangganAdp;
    HelperDb db=new HelperDb(this);
    Button btnTambahPel, btnPindahRek;
    EditText Cari;

    private static final String TAG = "PelangganList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.pelanggan_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listView=findViewById(R.id.lv_pelanggan);//id untuk layoutnya jangan sama dengan rekening karena akan crash
        btnTambahPel=findViewById(R.id.btn_tambah_pel);
        btnPindahRek=findViewById(R.id.btn_pindah_rek);
        Cari=findViewById(R.id.cari_nama_pel);

        btnTambahPel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pel=new Intent(PelangganList.this, Pelanggan.class);
                startActivity(pel);
            }
        });
        btnPindahRek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rek=new Intent(PelangganList.this, RekeningList.class);
                startActivity(rek);
            }
        });

        Cari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged: " + s.toString());
                getData(Cari.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        pelangganAdp = new PelangganAdp(PelangganList.this,pelanggans);
        listView.setDivider(null);
        listView.setAdapter(pelangganAdp);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final String kode = pelanggans.get(position).getId_pel();
                final String nama = pelanggans.get(position).getNama_pel();
                final String jenis = pelanggans.get(position).getJenis_rek();

                final CharSequence[] dialogItem={"Ubah","Hapus","Copy"};
                dialog=new AlertDialog.Builder(PelangganList.this);
                dialog.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which)
                        {
                            case 0:
                                Intent pel=new Intent(PelangganList.this, Pelanggan.class);
                                pel.putExtra("id_pel",kode);
                                pel.putExtra("nama_pel", nama);
                                pel.putExtra("jenis",jenis);
                                startActivity(pel);
                                break;
                            case 1:
                                konfirmHapus(kode);
                                break;
                            case 2:
                                copyToClipBoard(kode);
                                Cari.setText("");
                                break;
                        }
                    }
                }).show();
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        pelanggans.clear();
        if (Cari.getText().toString().equals("")){
            getData();
        }
        else {
            getData(Cari.getText().toString());
        }
    }

    private void getData() {
        ArrayList<HashMap<String,String>> daftar=db.getAll();

        pelanggans.clear();
        for(int i=0; i < daftar.size(); i++){
            String kode = daftar.get(i).get("id_pel");
            String nama = daftar.get(i).get("nama_pel");
            String jenis = daftar.get(i).get("jenis");

            ModelPelanggan pelanggan = new ModelPelanggan();
            pelanggan.setId_pel(kode);
            pelanggan.setNama_pel(nama);
            pelanggan.setJenis_rek(jenis);
            pelanggans.add(pelanggan);
        }
        pelangganAdp.notifyDataSetChanged();
    }

    protected void getData(String data){
        try {
            ArrayList<HashMap<String, String>> daftar = db.getAllByNama(data);
            if (daftar == null){
                daftar = new ArrayList<>();
            }

            pelanggans.clear();
            for(int i=0; i < daftar.size(); i++) {
                String kode=daftar.get(i).get("id_pel");
                String nama=daftar.get(i).get("nama_pel");
                String jenis=daftar.get(i).get("jenis");

                ModelPelanggan pelanggan = new ModelPelanggan();
                pelanggan.setId_pel(kode);
                pelanggan.setNama_pel(nama);
                pelanggan.setJenis_rek(jenis);
                pelanggans.add(pelanggan);
            }
            pelangganAdp.notifyDataSetChanged();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void copyToClipBoard(String data){
        String ambilData = data;
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("copy data", ambilData);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(this, "ID Pelanggan di Copy", Toast.LENGTH_SHORT).show();
    }

    private void konfirmHapus(String kode){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Konfirmasi Hapus")
                .setMessage("Yakin Data Ingin Dihapus?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.delete(kode);
                        pelanggans.clear();
                        getData();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false);
        alert.show();
    }
}