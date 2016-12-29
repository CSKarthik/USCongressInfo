package com.example.apoorva.hw9;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Apoorva on 11/23/2016.
 */

public class GetJSON {

    static String json="";
    static JSONObject jsonObject= null;

    public JSONObject fetchJSONObject(String reqURL ){
        HttpURLConnection connection =null;
        BufferedReader reader= null;
        try {
            //URL url = new URL("https://congress.api.sunlightfoundation.com/legislators?apikey=e04a4b41648649b9b7f347d870235068&per_page=all");
            URL url = new URL(reqURL);
            connection =(HttpURLConnection)url.openConnection();
            connection.connect();
            InputStream is= connection.getInputStream();
            reader= new BufferedReader(new InputStreamReader(is));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line=reader.readLine())!=null)
            {
                buffer.append(line);
            }
            is.close();
            json= buffer.toString();
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            jsonObject= new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        finally {
            if(connection!=null)
                connection.disconnect();
            try
            {
                if(reader!=null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }
}
