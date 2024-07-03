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
import com.ilham.siapel.adapter.RekeningAdp;
import com.ilham.siapel.helper.HelperDb;
import com.ilham.siapel.model.ModelRekening;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RekeningList extends AppCompatActivity {

    ListView listView;
    AlertDialog.Builder dialog;
    List<ModelRekening> rekenings = new ArrayList<>();
    RekeningAdp rekeningAdp;
    HelperDb db = new HelperDb(this);
    Button btnTambahRek, btnPindahPel;
    EditText CariRek;

    private static final String TAG = "RekeningList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.rekening_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listView = findViewById(R.id.lv_rekening);//id untuk layoutnya jangan sama dengan pelanggan karena akan crash
        btnTambahRek=findViewById(R.id.btn_tambah_rek);
        btnPindahPel=findViewById(R.id.btn_pindah_pel);
        CariRek=findViewById(R.id.cari_rekening);

        btnTambahRek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rek = new Intent(RekeningList.this, Rekening.class);
                startActivity(rek);
            }
        });
        btnPindahPel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pel = new Intent(RekeningList.this, PelangganList.class);
                startActivity(pel);
            }
        });

        CariRek.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged: " + s.toString());
                getDataRek(CariRek.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        rekeningAdp=new RekeningAdp(RekeningList.this, rekenings);
        listView.setDivider(null);
        listView.setAdapter(rekeningAdp);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final String kode_rek = rekenings.get(position).getId_rek();
                final String nama_rek = rekenings.get(position).getNama_rek();

                final CharSequence[] dialogItem = {"Ubah", "Hapus", "Salin"};
                dialog=new AlertDialog.Builder(RekeningList.this);
                dialog.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                Intent rek = new Intent(RekeningList.this, Rekening.class);
                                rek.putExtra("id_rek", kode_rek);
                                rek.putExtra("nama_rek", nama_rek);
                                startActivity(rek);
                                break;
                            case 1:
                                konfirmHapusRekening(kode_rek);
                                break;
                            case 2:
                                copyToClipboard(kode_rek);
                                CariRek.setText("");
                                break;
                        }
                    }
                }).show();
                return false;
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        rekenings.clear();

        if (CariRek.getText().toString().equals("")){
            getDataRek();
        } else {
            getDataRek(CariRek.getText().toString());
        }
    }

    public void getDataRek(){
        ArrayList<HashMap<String, String>> daftarRek = db.getAllRekening();
        rekenings.clear();
        for (int i = 0; i < daftarRek.size(); i++) {
            String kode_rek = daftarRek.get(i).get("id_rek");
            String nama_rek = daftarRek.get(i).get("nama_rek");

            ModelRekening rekening = new ModelRekening();
            rekening.setId_rek(kode_rek);
            rekening.setNama_rek(nama_rek);
            rekenings.add(rekening);
        }
        rekeningAdp.notifyDataSetChanged();
    }

    public void getDataRek(String data){
        try {
            ArrayList<HashMap<String, String>> daftarRek = db.getAllByNamaRekening(data);
            if (daftarRek == null) {
                daftarRek = new ArrayList<>();
            }

            rekenings.clear();
            for (int i = 0; i < daftarRek.size(); i++) {
                String kode_rek = daftarRek.get(i).get("id_rek");
                String nama_rek = daftarRek.get(i).get("nama_rek");

                ModelRekening rekening = new ModelRekening();
                rekening.setId_rek(kode_rek);
                rekening.setNama_rek(nama_rek);
                rekenings.add(rekening);
            }
            rekeningAdp.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void copyToClipboard(String datarek) {
        String ambilData = datarek;
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Copy Data", ambilData);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(RekeningList.this, "ID Rekening di Copy", Toast.LENGTH_SHORT).show();
    }

    private void konfirmHapusRekening(String kode_rek)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Konfirmasi Hapus")
                .setMessage("Yakin akan menghapus Data?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.deleteRekening(kode_rek);
                        rekenings.clear();
                        getDataRek();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false);
        alert.show();
    }

}