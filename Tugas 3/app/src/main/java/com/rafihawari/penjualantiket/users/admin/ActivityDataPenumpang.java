package com.rafihawari.penjualantiket.users.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.rafihawari.penjualantiket.R;
import com.rafihawari.penjualantiket.users.adapter.AdapterPenumpang;
import com.rafihawari.penjualantiket.users.model.ModelPenumpang;
import com.rafihawari.penjualantiket.users.server.BaseURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityDataPenumpang extends AppCompatActivity {

    ProgressDialog pDialog;

    AdapterPenumpang adapter;
    ListView list;

    ArrayList<ModelPenumpang> newsList = new ArrayList<ModelPenumpang>();
    private RequestQueue mRequestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_data_penumpang );

        getSupportActionBar().setTitle("Data Penumpang");
        mRequestQueue = Volley.newRequestQueue(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        list = (ListView) findViewById(R.id.array_list);
        newsList.clear();
        adapter = new AdapterPenumpang(ActivityDataPenumpang.this, newsList);
        list.setAdapter(adapter);
        getAllBuku();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ActivityDataPenumpang.this, HomeAdminActivity.class);
        startActivity(i);
        finish();
    }

    private void getAllBuku() {
        // Pass second argument as "null" for GET requests
        pDialog.setMessage("Loading");
        showDialog();
        JsonObjectRequest req = new JsonObjectRequest( Request.Method.GET, BaseURL.datapenumpang, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
                        try {
                            boolean status = response.getBoolean("error");
                            if (status == false) {
                                Log.d("data buku = ", response.toString());
                                String data = response.getString("data");
                                JSONArray jsonArray = new JSONArray(data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    final ModelPenumpang penumpang = new ModelPenumpang();
                                    final String _id = jsonObject.getString("_id");
                                    final String kodePenumpang = jsonObject.getString("kodePenumpang");
                                    final String namaPenumpang = jsonObject.getString("namaPenumpang");
                                    final String umur = jsonObject.getString("umur");
                                    final String tujuan = jsonObject.getString("tujuan");
                                    final String jamBerangkat = jsonObject.getString("jamBerangkat");
                                    final String tanggalBerangkat = jsonObject.getString("tanggalBerangkat");
                                    final String hargaTiket = jsonObject.getString("hargaTiket");
                                    final String gambar = jsonObject.getString("gambar");
                                    penumpang.setKodePenumpang(kodePenumpang);
                                    penumpang.setNamaPenumpang(namaPenumpang);
                                    penumpang.setUmur(umur);
                                    penumpang.setTujuan(tujuan);
                                    penumpang.setJamBerangkat(jamBerangkat);
                                    penumpang.setTanggalBerangkat(tanggalBerangkat);
                                    penumpang.setHargaTiket(hargaTiket);
                                    penumpang.setGambar(gambar);
                                    penumpang.set_id(_id);

                                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            // TODO Auto-generated method stub
                                            Intent a = new Intent(ActivityDataPenumpang.this, EditPenumpangDanHapusActivity.class);
                                            a.putExtra("kodePenumpang", newsList.get(position).getKodePenumpang());
                                            a.putExtra("_id", newsList.get(position).get_id());
                                            a.putExtra("namaPenumpang", newsList.get(position).getNamaPenumpang());
                                            a.putExtra("umur", newsList.get(position).getUmur());
                                            a.putExtra("tujuan", newsList.get(position).getTujuan());
                                            a.putExtra("jamBerangkat", newsList.get(position).getJamBerangkat());
                                            a.putExtra("tanggalBerangkat", newsList.get(position).getTanggalBerangkat());
                                            a.putExtra("hargaTiket", newsList.get(position).getHargaTiket());
                                            a.putExtra("gambar", newsList.get(position).getGambar());
                                            startActivity(a);
                                        }
                                    });
                                    newsList.add(penumpang);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                hideDialog();
            }
        });

        /* Add your Requests to the RequestQueue to execute */
        mRequestQueue.add(req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}