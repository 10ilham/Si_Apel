package com.ilham.siapel.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HelperDb extends SQLiteOpenHelper {

    final static String NAMA_DATABASE="siapel";
    final static int DATABASE_VERSION=1;

    static String NAMA_TABEL="pelanggan";
    static String NAMA_TABEL_REKENING="rekening";

    public HelperDb(Context context){
        super(context, NAMA_DATABASE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String BUAT_TABEL="CREATE TABLE " + NAMA_TABEL + " (id_pelanggan TEXT PRIMARY KEY NOT NULL," +
                "nama_pelanggan TEXT NOT NULL, jenis_rek TEXT NOT NULL)";
        db.execSQL(BUAT_TABEL);

        //Membuat tabel rekening
        final String BUAT_TABEL_REKENING="CREATE TABLE " + NAMA_TABEL_REKENING + " (id_rekening TEXT PRIMARY KEY NOT NULL," +
                "nama_rekening TEXT NOT NULL)";
        db.execSQL(BUAT_TABEL_REKENING);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NAMA_TABEL);
        db.execSQL("DROP TABLE IF EXISTS " + NAMA_TABEL_REKENING); //Untuk Tabel rekening
        onCreate(db);
    }

    public void insert(String id, String nama, String jenis){
        String QUERY="INSERT INTO " + NAMA_TABEL + " VALUES('" + id + "','" + nama + "','" + jenis + "')";
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL(QUERY);
    }
    public void update(String id, String nama, String jenis, String old_id){
        String QUERY="UPDATE " + NAMA_TABEL + " SET id_pelanggan='" + id + "',nama_pelanggan='" +
                nama + "',jenis_rek='" + jenis + "' WHERE id_pelanggan='" + old_id + "'";
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL(QUERY);
    }
    public void delete(String id){
        String QUERY="DELETE FROM " + NAMA_TABEL + " WHERE id_pelanggan='" + id + "'";
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL(QUERY);
    }

    public ArrayList<HashMap<String,String>> getAll(){
        ArrayList<HashMap<String,String>> daftar=new ArrayList<>();
        String QUERY="SELECT * FROM " + NAMA_TABEL;
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery(QUERY,null);

        if (cursor.moveToFirst()){
            do {
                HashMap<String,String> temp=new HashMap<>();
                temp.put("id_pel",cursor.getString(0));
                temp.put("nama_pel",cursor.getString(1));
                temp.put("jenis",cursor.getString(2));
                daftar.add(temp);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return daftar;
    }

    public ArrayList<HashMap<String,String>> getAllByNama(String nama){
        ArrayList<HashMap<String,String>> daftar=new ArrayList<>();
        String QUERY="SELECT * FROM " + NAMA_TABEL + " WHERE nama_pelanggan LIKE '%" + nama + "%'";
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery(QUERY,null);

        if (cursor.moveToFirst()){
            do {
                HashMap<String,String> temp=new HashMap<>();
                temp.put("id_pel",cursor.getString(0));
                temp.put("nama_pel",cursor.getString(1));
                temp.put("jenis",cursor.getString(2));
                daftar.add(temp);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return daftar;
    }

    public boolean isExists(String kd){
        boolean cek=false;
        String QUERY="SELECT * FROM " + NAMA_TABEL + " WHERE id_pelanggan='" + kd + "'";
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery(QUERY,null);

        if (cursor.getCount()>0){
            cek=true;
        }
        return cek;
    }

    //Membuat CRUD untuk Tabel Rekening
    public void insertRekening(String id_rek, String nama_rek){
        String QUERY = "INSERT INTO " + NAMA_TABEL_REKENING + " VALUES('" + id_rek + "','" + nama_rek + "')";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(QUERY);
    }
    public void updateRekening(String id_rek, String nama_rek, String old_id_rek) {
        String QUERY = "UPDATE " + NAMA_TABEL_REKENING + " SET id_rekening='" + id_rek + "', nama_rekening='" +
                nama_rek + "' WHERE id_rekening='" + old_id_rek + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(QUERY);
    }
    public void deleteRekening(String id_rek) {
        String QUERY = "DELETE FROM " + NAMA_TABEL_REKENING + " WHERE id_rekening='" + id_rek + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(QUERY);
    }

    public ArrayList<HashMap<String, String>> getAllRekening()
    {
        ArrayList<HashMap<String, String>> daftarRek = new ArrayList<>();
        String QUERY = "SELECT * FROM " + NAMA_TABEL_REKENING;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor.moveToFirst())
        {
            do {
                HashMap<String, String> temp = new HashMap<>();
                temp.put("id_rek", cursor.getString(0));
                temp.put("nama_rek", cursor.getString(1));
                daftarRek.add(temp);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return daftarRek;
    }

    //Untuk menampilkan id dan nama rekening pada layout list_rekening
    public ArrayList<HashMap<String, String>> getAllByNamaRekening(String nama_rek)
    {
        ArrayList<HashMap<String, String>> daftarRek = new ArrayList<>();
        String QUERY = "SELECT * FROM " + NAMA_TABEL_REKENING + " WHERE nama_rekening LIKE '%" + nama_rek + "%'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor.moveToFirst())
        {
            do {
                HashMap<String, String> temp = new HashMap<>();
                temp.put("id_rek", cursor.getString(0));
                temp.put("nama_rek", cursor.getString(1));
                daftarRek.add(temp);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return daftarRek;
    }

    //Untuk mengisi isi jenis pada pelanggan
    public List<String> getAllNamaRekening() {
        List<String> namaRekenings = new ArrayList<>();
        String QUERY = "SELECT nama_rekening FROM " + NAMA_TABEL_REKENING;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                namaRekenings.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return namaRekenings;
    }

    public boolean isExistsRekening(String kode_rek){
        boolean cek=false;
        String QUERY="SELECT * FROM " + NAMA_TABEL_REKENING + " WHERE id_rekening='" + kode_rek + "'";
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery(QUERY,null);

        if (cursor.getCount()>0){
            cek=true;
        }
        return cek;
    }
}
