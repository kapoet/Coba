package com.example.ervin.coba;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

/**
 * Created by ervin on 5/27/2017.
 * bisa dibilang sebagai kelas adapter atau kelas holder yang bakal menanagani proses wadah recycleview
 */

class TunjukanDataHolder extends RecyclerView.ViewHolder {
    private final TextView nama;
    private final ImageView gambar;

    public TunjukanDataHolder(View itemView) {
        super(itemView);
        nama = (TextView) itemView.findViewById(R.id.namaGamabrStorage);
        gambar = (ImageView) itemView.findViewById(R.id.gambarStorage);
    }


    public void judul_gambar(String name) {
        nama.setText(name);
    }

    public void link_gambar(String linkgambar) {
        Glide.with(itemView.getContext()).load(linkgambar).into(gambar);
    }
}