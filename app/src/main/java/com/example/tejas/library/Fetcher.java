package com.example.tejas.library;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Fetcher {

    private static final String TAG = "Fetchr";

private final String areaId = "ag9ifmdsb2JhbGNpdHktMjByEQsSBEFyZWEYgICAgM7fhwoM";
    //returns raw data from url and returns it as a array of bytes
    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    //convert the result of getUrlBytes() method to string
    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }


    public List<Brand> fetchItems() {
        List<Brand> items = new ArrayList<>();

        try {
            String url = Uri.parse("http://globalcity-20.appspot.com/api/v1/brand")
                    .buildUpon().appendQueryParameter("areaId",areaId).build()
                    .toString();
           String jsonString = getUrlString(url);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(items,jsonBody);
          //  Log.i(TAG, "Received JSON: " + jsonString);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        }
        catch (JSONException jo){
            Log.e(TAG, "Failed to fetch items", jo);

        }
        return  items;
    }

    private void parseItems(List<Brand> items,JSONObject jsonBody)
            throws IOException, JSONException {

        JSONArray jsonArray = jsonBody.getJSONArray("brands");
        for(int i=0;i < jsonArray.length();i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String brandName = jsonObject.getString("brandName");
            Brand brand = new Brand(brandName);
            brand.setBrandName(brandName);
            items.add(brand);

        }

    }

}
