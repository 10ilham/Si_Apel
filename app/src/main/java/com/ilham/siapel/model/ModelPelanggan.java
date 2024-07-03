package com.ilham.siapel.model;

public class ModelPelanggan {
    private String id_pel;
    private String nama_pel;
    private String jenis_rek;

    public ModelPelanggan() {
        this.id_pel = id_pel;
        this.nama_pel = nama_pel;
        this.jenis_rek = jenis_rek;
    }

    public String getId_pel() {
        return id_pel;
    }

    public void setId_pel(String id_pel) {
        this.id_pel = id_pel;
    }

    public String getNama_pel() {
        return nama_pel;
    }

    public void setNama_pel(String nama_pel) {
        this.nama_pel = nama_pel;
    }

    public String getJenis_rek() {
        return jenis_rek;
    }

    public void setJenis_rek(String jenis_rek) {
        this.jenis_rek = jenis_rek;
    }
}
