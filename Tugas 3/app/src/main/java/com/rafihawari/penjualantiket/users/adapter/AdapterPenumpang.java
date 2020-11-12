package com.rafihawari.penjualantiket.users.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rafihawari.penjualantiket.R;
import com.rafihawari.penjualantiket.users.model.ModelPenumpang;
import com.rafihawari.penjualantiket.users.server.BaseURL;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterPenumpang extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<ModelPenumpang> item;

    public AdapterPenumpang(Activity activity, List<ModelPenumpang> item) {
        this.activity = activity;
        this.item = item;
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int position) {
        return item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService( Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate( R.layout.content_penumpang, null);


        TextView namaPenumpang      = (TextView) convertView.findViewById(R.id.txtnamaPenumpang);
        TextView umur               = (TextView) convertView.findViewById(R.id.txtUmur);
        TextView tujuan             = (TextView) convertView.findViewById(R.id.txtTujuan);
        TextView jamBerangkat       = (TextView) convertView.findViewById(R.id.txtJamBerangkat);
        TextView hargaTiket         = (TextView) convertView.findViewById(R.id.txtHargaTiket);
        TextView tanggalBerangkat   = (TextView) convertView.findViewById(R.id.txtTanggalBerangkat);
        ImageView gambarPenumpang   = (ImageView) convertView.findViewById(R.id.gambarPenumpang);

        namaPenumpang.setText(item.get(position).getNamaPenumpang());
        umur.setText(item.get(position).getUmur());
        tujuan.setText(item.get(position).getTujuan());
        jamBerangkat.setText(item.get(position).getJamBerangkat());
        hargaTiket.setText("Rp." + item.get(position).getHargaTiket());
        tanggalBerangkat.setText(item.get(position).getTanggalBerangkat());
        Picasso.get().load( BaseURL.baseUrl + "gambar/" + item.get(position).getGambar())
                .resize(40, 40)
                .centerCrop()
                .into(gambarPenumpang);
        return convertView;
    }

}
