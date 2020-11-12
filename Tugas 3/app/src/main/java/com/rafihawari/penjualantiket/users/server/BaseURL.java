package com.rafihawari.penjualantiket.users.server;

public class BaseURL {

    public static String baseUrl = "http://192.168.43.135:5050/";

    public static String login      = baseUrl + "user/login";
    public static String register   = baseUrl + "user/registrasi";

    //Penumpang
    public static String datapenumpang = baseUrl + "penumpang/datapenumpang";
    public static String editDataPenumpang = baseUrl + "penumpang/ubah/";
    public static String hapusData = baseUrl + "penumpang/hapus/";
    public static String inputPenumpang = baseUrl + "penumpang/input";

}
