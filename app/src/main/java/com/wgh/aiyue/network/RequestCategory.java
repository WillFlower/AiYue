package com.wgh.aiyue.network;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by   : WGH.
 */
public class RequestCategory extends Request<ArrayList<String>> {

    private Response.Listener<ArrayList<String>> listener;
    private int SOURCE = -1;

    public RequestCategory(String url, Response.Listener<ArrayList<String>> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = listener;
    }

    @Override
    protected Response<ArrayList<String>> parseNetworkResponse(NetworkResponse response) {
        try {
            ArrayList<String> contents = parseImgSession(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(response.data))));
            return Response.success(contents, HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(ArrayList<String> response) {
        listener.onResponse(response);
    }

    private ArrayList<String> parseImgSession(BufferedReader bufferedReader) {
        ArrayList<String> contents = new ArrayList<>();
        // to deal with the response data.
        return contents;
    }
}
