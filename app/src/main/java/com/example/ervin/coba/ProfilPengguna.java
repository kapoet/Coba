package com.example.ervin.coba;

/**
 * Created by ervin on 5/23/2017.
 */

public class ProfilPengguna {
    String nama,
    email,
    pekerjaan;

    public ProfilPengguna() {
    }

    public ProfilPengguna(String nama, String email, String pekerjaan) {
        this.nama = nama;
        this.email = email;
        this.pekerjaan = pekerjaan;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPekerjaan() {
        return pekerjaan;
    }

    public void setPekerjaan(String pekerjaan) {
        this.pekerjaan = pekerjaan;
    }
}
