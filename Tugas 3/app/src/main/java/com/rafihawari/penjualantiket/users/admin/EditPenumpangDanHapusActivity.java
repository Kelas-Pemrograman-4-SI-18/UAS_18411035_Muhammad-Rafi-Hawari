package com.rafihawari.penjualantiket.users.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.rafihawari.penjualantiket.R;
import com.rafihawari.penjualantiket.users.server.BaseURL;
import com.rafihawari.penjualantiket.users.server.VolleyMultipart;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditPenumpangDanHapusActivity extends AppCompatActivity {

    EditText edtKodePenumpang, edtNamaPenumpang, edtUmur, edtTujuan, edtTanggalBerangkat, edtJamBerangkat, edtHargaTiket;
    ImageView imgGambarPenumpang;
    Button btnTakeImg, editData, btnUploadGambar, hapusData;

    String strKodePenumpang, strNamaPenumpang, strUmur, strTujuan, strTanggalBerangkat, strJamBerangkat, strHargaTiket, strGambar, _id;

    private RequestQueue mRequestQueue;

    Bitmap bitmap;

    private final int CameraR_PP = 1;
    String mCurrentPhotoPath;

    ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_edit_penumpang_dan_hapus );
        getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mRequestQueue = Volley.newRequestQueue(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        edtKodePenumpang = (EditText) findViewById(R.id.edtKodePenumpang);
        edtNamaPenumpang = (EditText) findViewById(R.id.edtNamaPenumpang);
        edtUmur = (EditText) findViewById(R.id.edtUmur);
        edtTujuan = (EditText) findViewById(R.id.edtTujuan);
        edtTanggalBerangkat= (EditText) findViewById(R.id.edtTanggalBerangkat);
        edtJamBerangkat= (EditText) findViewById(R.id.edtJamBerangkat);
        edtHargaTiket = (EditText) findViewById(R.id.edtHargaTiket);

        imgGambarPenumpang = (ImageView) findViewById(R.id.gambar);

        btnTakeImg = (Button) findViewById(R.id.btnTakeImage);
        editData = (Button) findViewById(R.id.editData);
        btnUploadGambar = (Button) findViewById(R.id.btnUploadGambar);
        hapusData = (Button) findViewById(R.id.hapusData);

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

        btnTakeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeImage();
            }
        });
        editData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDataTanpaGambar();
            }
        });

        btnUploadGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDataDenganGambar(bitmap);
            }
        });

        hapusData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditPenumpangDanHapusActivity.this);

                builder.setTitle("Konfirmasi");
                builder.setMessage("Yakin ingin menghapus buku " + strNamaPenumpang + " ? ");

                builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        hapusData();
                    }
                });
                builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }
        });
    }

    public void takeImage(){
        addPermission();
        Intent cameraIntent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(EditPenumpangDanHapusActivity.this.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.i("Tags", "IOException");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(cameraIntent, CameraR_PP);
            }
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == EditPenumpangDanHapusActivity.RESULT_CANCELED) {
            return;
        }
        if (requestCode == CameraR_PP) {
            try {

                bitmap = MediaStore.Images.Media.getBitmap(EditPenumpangDanHapusActivity.this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
                imgGambarPenumpang.setImageBitmap(bitmap);
//                uploadPotoProfile(bitmap);
                if (imgGambarPenumpang.getDrawable() != null) {
                    int newHeight = 300; // New height in pixels
                    int newWidth = 300;
                    imgGambarPenumpang.requestLayout();
                    imgGambarPenumpang.getLayoutParams().height = newHeight;
                    // Apply the new width for ImageView programmatically
                    imgGambarPenumpang.getLayoutParams().width = newWidth;
                    // Set the scale type for ImageView image scaling
                    imgGambarPenumpang.setScaleType(ImageView.ScaleType.FIT_XY);
                    ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) imgGambarPenumpang.getLayoutParams();
                    marginParams.setMargins(0, 10, 0, 0);
                    btnUploadGambar.setVisibility(View.VISIBLE);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(EditPenumpangDanHapusActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void addPermission() {
        Dexter.withActivity(EditPenumpangDanHapusActivity.this)
                .withPermissions(

                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            //Toast.makeText(getActivity(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(EditPenumpangDanHapusActivity.this, "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (bitmap != null){
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
        }
        return byteArrayOutputStream.toByteArray();
    }

    private void editDataDenganGambar(final Bitmap bitmap) {

        pDialog.setMessage("Mohon Tunggu Sedang Mengupload Gambar !!!!!");
        showDialog();
        VolleyMultipart volleyMultipartRequest = new VolleyMultipart( Request.Method.PUT, BaseURL.editDataPenumpang + _id,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        mRequestQueue.getCache().clear();
                        hideDialog();
                        try {
                            JSONObject jsonObject = new JSONObject(new String(response.data));
                            System.out.println("ress = " + jsonObject.toString());
                            String strMsg = jsonObject.getString("msg");
                            boolean status= jsonObject.getBoolean("error");
                            System.out.println( "response server = " + jsonObject.toString() );
                            if(status == false){
                                Toast.makeText(getApplicationContext(), strMsg, Toast.LENGTH_LONG).show();
                                Intent i = new Intent(EditPenumpangDanHapusActivity.this, ActivityDataPenumpang.class);
                                startActivity(i);
                                finish();
                            }else {
                                Toast.makeText(getApplicationContext(), strMsg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideDialog();
                        Toast.makeText(EditPenumpangDanHapusActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("kodePenumpang", edtKodePenumpang.getText().toString());
                params.put("namaPenumpang", edtNamaPenumpang.getText().toString());
                params.put("umur", edtUmur.getText().toString());
                params.put("tujuan", edtTujuan.getText().toString());
                params.put("tanggalBerangkat", edtTanggalBerangkat.getText().toString());
                params.put("jamBerangkat", edtJamBerangkat.getText().toString());
                params.put("hargaTiket", edtHargaTiket.getText().toString());
                return params;
            }
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("gambar", new VolleyMultipart.DataPart(imagename + ".jpg", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue = Volley.newRequestQueue(EditPenumpangDanHapusActivity.this);
        mRequestQueue.add(volleyMultipartRequest);
    }

    public void editDataTanpaGambar(){

        // Post params to be sent to the server
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("kodePenumpang", edtKodePenumpang.getText().toString());
        params.put("namaPenumpang", edtNamaPenumpang.getText().toString());
        params.put("umur", edtUmur.getText().toString());
        params.put("tujuan", edtTujuan.getText().toString());
        params.put("tanggalBerangkat", edtTanggalBerangkat.getText().toString());
        params.put("jamBerangkat", edtJamBerangkat.getText().toString());
        params.put("hargaTiket", edtHargaTiket.getText().toString());
        params.put("gambar", strGambar);

        pDialog.setMessage("Mohon Tunggu.....");
        showDialog();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT,BaseURL.editDataPenumpang + _id, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
                        try {
                            String strMsg = response.getString("msg");
                            boolean status= response.getBoolean("error");
                            if(status == false){
                                Toast.makeText(getApplicationContext(), strMsg, Toast.LENGTH_LONG).show();
                                Intent i = new Intent(EditPenumpangDanHapusActivity.this, ActivityDataPenumpang.class);
                                startActivity(i);
                                finish();
                            }else {
                                Toast.makeText(getApplicationContext(), strMsg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                hideDialog();
            }
        });
        mRequestQueue.add(req);
    }

    public void hapusData(){

        pDialog.setMessage("Mohon Tunggu");
        showDialog();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.DELETE,BaseURL.hapusData + _id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
                        try {
                            String strMsg = response.getString("msg");
                            boolean status= response.getBoolean("error");
                            if(status == false){
                                Toast.makeText(getApplicationContext(), strMsg, Toast.LENGTH_LONG).show();
                                Intent i = new Intent(EditPenumpangDanHapusActivity.this, ActivityDataPenumpang.class);
                                startActivity(i);
                                finish();
                            }else {
                                Toast.makeText(getApplicationContext(), strMsg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                hideDialog();
            }
        });
        mRequestQueue.add(req);
    }

    private void showDialog(){
        if(!pDialog.isShowing()){
            pDialog.show();
        }
    }

    private void hideDialog(){
        if(pDialog.isShowing()){
            pDialog.dismiss();
        }
    }
}