package com.ilham.siapel.aktiviti;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ilham.siapel.R;
import com.ilham.siapel.helper.HelperDb;

public class Rekening extends AppCompatActivity {

    private EditText inIdRek, inNamaRek;
    private Button btnSimpanRek;
    private HelperDb db=new HelperDb(this);
    String kode_rek, nama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.rekening);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inIdRek=findViewById(R.id.in_id_rekening);
        inNamaRek=findViewById(R.id.in_nama_rekening);
        btnSimpanRek=findViewById(R.id.btn_simpan_rek);

        kode_rek=getIntent().getStringExtra("id_rek");
        nama=getIntent().getStringExtra("nama_rek");

        if (kode_rek==null || kode_rek.equals("")){
            setTitle("Tambah Rekening");
            inIdRek.requestFocus();
        } else {
            setTitle("Ubah Rekening");
            inIdRek.setText(kode_rek);
            inNamaRek.setText(nama);
        }

        btnSimpanRek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kode_rek == null || kode_rek.equals("")){
                    simpanRekening();
                } else {
                    ubahRekening(kode_rek);
                }
            }
        });
    }

    private void simpanRekening(){
        if (inIdRek.getText().toString().equals("") || inNamaRek.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Silahkan Isi Semua Data!", Toast.LENGTH_SHORT).show();
        } else
        {
            if (!db.isExistsRekening(inIdRek.getText().toString()))
            {
                db.insertRekening(inIdRek.getText().toString(), inNamaRek.getText().toString());
                finish();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "ID Rekening Sudah Terdaftar!", Toast.LENGTH_SHORT).show();
                inIdRek.selectAll();
                inIdRek.requestFocus();
            }
        }
    }
    private void ubahRekening(String kode)
    {
        if (inIdRek.getText().toString().equals("")||inNamaRek.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Silahkan Isi Semua Data!",Toast.LENGTH_SHORT).show();
        }
        else
        {
            if (db.isExistsRekening(kode_rek))
            {
                db.updateRekening(inIdRek.getText().toString(), inNamaRek.getText().toString(), kode);
                finish();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "ID Rekening Tidak Terdaftar!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}