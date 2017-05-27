package com.example.ervin.coba;

/**
 * Created by ervin on 5/27/2017.
 */

public class ItemGambar {
    String nama_gambar, link_gambar;

    public ItemGambar(String link_gambar,String nama_gambar) {
        this.link_gambar = link_gambar;
        this.nama_gambar = nama_gambar;

    }

    public ItemGambar() {
    }

    public String getNama_gambar() {
        return nama_gambar;
    }

    public void setNama_gambar(String nama_gambar) {
        this.nama_gambar = nama_gambar;
    }

    public String getLink_gambar() {
        return link_gambar;
    }

    public void setLink_gambar(String link_gambar) {
        this.link_gambar = link_gambar;
    }
}
