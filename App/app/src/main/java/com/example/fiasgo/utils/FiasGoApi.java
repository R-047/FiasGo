package com.example.fiasgo.utils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class FiasGoApi {

//    localhost: http://192.168.1.101:3000/
//    heroku: https://fias-go.herokuapp.com/
    private static final String BASE_URL = "https://fias-go.herokuapp.com/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        System.out.println("GET REQUEST SENDING: "+User.getUser().session_id);
        client.addHeader("Cookie",User.getUser().session_id);
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        System.out.println("GET REQUEST SENDING: "+User.getUser().session_id);
        client.addHeader("Cookie",User.getUser().session_id);
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }



    public static String getSesssionFromResponseHeaders(Header[] headers){
        for(Header header : headers){
            if(header.getName().equals("Set-Cookie")){
                return header.getValue().split(";")[0];
            }
        }
        return  null;
    }



}
