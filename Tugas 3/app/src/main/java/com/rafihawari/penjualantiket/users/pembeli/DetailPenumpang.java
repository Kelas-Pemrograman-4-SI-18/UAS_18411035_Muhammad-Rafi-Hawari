package com.rafihawari.penjualantiket.users.pembeli;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.rafihawari.penjualantiket.R;
import com.rafihawari.penjualantiket.users.server.BaseURL;
import com.squareup.picasso.Picasso;

public class DetailPenumpang extends AppCompatActivity {

    EditText edtKodePenumpang, edtNamaPenumpang, edtUmur, edtTujuan, edtTanggalBerangkat, edtJamBerangkat, edtHargaTiket;
    ImageView imgGambarPenumpang;

    String strKodePenumpang, strNamaPenumpang, strUmur, strTujuan, strTanggalBerangkat, strJamBerangkat, strHargaTiket, strGambar, _id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_detail_penumpang );

        edtKodePenumpang = (EditText) findViewById(R.id.edtKodePenumpang);
        edtNamaPenumpang = (EditText) findViewById(R.id.edtNamaPenumpang);
        edtUmur = (EditText) findViewById(R.id.edtUmur);
        edtTujuan = (EditText) findViewById(R.id.edtTujuan);
        edtTanggalBerangkat = (EditText) findViewById(R.id.edtTanggalBerangkat);
        edtJamBerangkat = (EditText) findViewById(R.id.edtJamBerangkat);
        edtHargaTiket = (EditText) findViewById(R.id.edtHargaTiket);

        imgGambarPenumpang = (ImageView) findViewById(R.id.gambar);

        Intent i = getIntent();
        strKodePenumpang = i.getStringExtra("kodePenumpang");
        strNamaPenumpang = i.getStringExtra("namaPenumpang");
        strUmur = i.getStringExtra("umur");
        strTujuan = i.getStringExtra("tujuan");
        strTanggalBerangkat = i.getStringExtra("tanggalBerangkat");
        strJamBerangkat = i.getStringExtra("jamBerangkat");
        strHargaTiket = i.getStringExtra("hargaTiket");
        strGambar = i.getStringExtra("gambar");
        _id = i.getStringExtra("_id");

        edtKodePenumpang.setText(strKodePenumpang);
        edtNamaPenumpang.setText(strNamaPenumpang);
        edtUmur.setText(strUmur);
        edtTujuan.setText(strTujuan);
        edtTanggalBerangkat.setText(strTanggalBerangkat);
        edtJamBerangkat.setText(strJamBerangkat);
        edtHargaTiket.setText(strHargaTiket);
        Picasso.get().load( BaseURL.baseUrl + "gambar/" + strGambar)
                .into(imgGambarPenumpang);
    }
}