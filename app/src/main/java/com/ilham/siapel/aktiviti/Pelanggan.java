package com.ilham.siapel.aktiviti;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.ilham.siapel.R;
import com.ilham.siapel.helper.HelperDb;

import java.util.ArrayList;
import java.util.List;

public class Pelanggan extends AppCompatActivity {
    private EditText inpId, inpNama;
    private Spinner inpJenis;
    private Button btnSimpan;
    private HelperDb db=new HelperDb(this);
    String kd,nm,jn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.pelanggan);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inpId=findViewById(R.id.in_id_pelanggan);//id untuk layoutnya jangan sama dengan rekening karena akan crash
        inpNama=findViewById(R.id.in_nama_pelanggan);
        inpJenis=findViewById(R.id.in_jenis_rekening);
        btnSimpan=findViewById(R.id.btn_simpan_pel);

        kd = getIntent().getStringExtra("id_pel");
        nm = getIntent().getStringExtra("nama_pel");
        jn = getIntent().getStringExtra("jenis");

        isiJenis();

        if(kd==null || kd.equals("")){
            setTitle("Tambah Pelanggan");
            inpId.requestFocus();
        } else{
            setTitle("Ubah Pelanggan");
            inpId.setText(kd);
            inpNama.setText(nm);
            setSpinnerSelection(inpJenis, jn); // Atur item yang dipilih di Spinner
        }

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kd == null || kd.equals("")){
                    simpan();
                } else {
                    ubah(kd);
                }
            }
        });
    }

    public void isiJenis(){
        List<String> jenis = db.getAllNamaRekening(); // Ambil nama rekening dari database

        jenis.add(0, "Pilih Jenis Rekening");
        ArrayAdapter<String> combo=new ArrayAdapter<>(Pelanggan.this,
                android.R.layout.simple_spinner_item, jenis);// Memperbaiki pengaturan ArrayAdapter
        combo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inpJenis.setAdapter(combo);
    }

    private void setSpinnerSelection(Spinner spinner, String value) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
        int position = adapter.getPosition(value);
        spinner.setSelection(position);
    }

    public void simpan(){
        if (inpId.getText().toString().equals("") || inpNama.getText().toString().equals("") || inpJenis.getSelectedItem().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Silahkan isi semua data!", Toast.LENGTH_SHORT).show();
        } else{
            if (!db.isExists(inpId.getText().toString())){
                db.insert(inpId.getText().toString(), inpNama.getText().toString(), inpJenis.getSelectedItem().toString());
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "ID Pelanggan sudah terdaftar", Toast.LENGTH_SHORT).show();
                inpId.selectAll();
                inpId.requestFocus();
            }
        }
    }

    public void ubah(String kode){
        if (inpId.getText().toString().equals("")||inpNama.getText().toString().equals("")||inpJenis.getSelectedItem().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Silahkan isi semua data!", Toast.LENGTH_SHORT).show();
        } else {
            if (db.isExists(kd)){
                db.update(inpId.getText().toString(), inpNama.getText().toString(), inpJenis.getSelectedItem().toString(), kode);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "ID pelanggan tidak terdaftar!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}