package com.example.apoorva.hw9;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Karthik Prakash on 11/28/2016.
 */

public class BillDetails extends Activity{ ImageView star;
    TextView billId,billType,title,sponsor,chamber,status,introducedOn,congressURL,version,billURL;
    ImageView toolbarBack;
    JSONObject jsonObj;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details);

        try {
            jsonObj = new JSONObject(getIntent().getBundleExtra("jsonObject").getString("jsonObject"));
            BillJSON billJSON = setJSONValue();
            setImagesOnClick(billJSON);
            setValueOfJSON(billJSON);
            toolbarBack = (ImageView)findViewById(R.id.toolbarBack);
            toolbarBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    //finishActivity(1);
                }
            });
        } catch (Exception e) {
            System.out.print(e.getStackTrace());
        }
    }

    public void setImagesOnClick(final BillJSON billJSON) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        star=(ImageView)findViewById(R.id.star);
        if( MainActivity.fav_bill_json == null|| !MainActivity.fav_bill_json.has(billJSON.getBillId())) {
            star.setImageResource(R.drawable.whitestar);
        } else {
            star.setImageResource(R.drawable.yellowstar);
        }

        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CHANGE STAR COLOR IF REQUIRED
                if(!MainActivity.fav_bill_json.has(billJSON.getBillId())){
                    try {
                        MainActivity.fav_bill_json.put(billJSON.getBillId(), jsonObj.toString());

                        SharedPreferences settings = getSharedPreferences("FavFile", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("bill", MainActivity.fav_bill_json.toString());
                        editor.commit();
                            /*Toast.makeText(this, MainActivity.fav_legislator_json.toString(), Toast.LENGTH_SHORT).show()*/
                        Log.d("bill", MainActivity.fav_bill_json.toString());
                        star.setImageResource(R.drawable.yellowstar);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    MainActivity.fav_bill_json.remove(billJSON.getBillId());
                    SharedPreferences settings = getSharedPreferences("FavFile", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("bill", MainActivity.fav_bill_json.toString());
                    editor.commit();
                    star.setImageResource(R.drawable.whitestar);
                }
            }
        });
        congressURL = (TextView)findViewById(R.id.congressURLValue);
        congressURL.setText(billJSON.getCongressURL());
        congressURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(billJSON.getCongressURL())));
                } catch(Exception e) {}
            }
        });
        billURL = (TextView)findViewById(R.id.billURLValue);
        billURL.setText(billJSON.getBillURL());
        billURL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(billJSON.getBillURL())));
                } catch(Exception e) {}
            }
        });

    }

    public void setValueOfJSON(BillJSON billJSON) {
        billId = (TextView)findViewById(R.id.billIdValue);
        billId.setText(billJSON.getBillId());
        System.out.println(billId);

        billType = (TextView)findViewById(R.id.billTypeValue);
        billType.setText(billJSON.getBillType());
        System.out.println(billType);

        chamber = (TextView)findViewById(R.id.chamberValue);
        chamber.setText(billJSON.getChamber());
        System.out.println(chamber);

        introducedOn = (TextView)findViewById(R.id.introducedOnValue);
        introducedOn.setText(billJSON.getIntroducedOn());
        System.out.println(introducedOn);

        title = (TextView)findViewById(R.id.titleValue);
        title.setText(billJSON.getTitle());
        System.out.println(title);

        sponsor = (TextView)findViewById(R.id.sponsorValue);
        sponsor.setText(billJSON.getSponsor());
        System.out.println(sponsor);

        status = (TextView)findViewById(R.id.statusValue);
        status.setText(billJSON.getStatus());
        System.out.println(status);



        System.out.println(congressURL);


        System.out.println(billURL);


        version = (TextView)findViewById(R.id.versionValue);
        version.setText(billJSON.getVersion());
        System.out.println(version);

    }


    public BillJSON setJSONValue() throws Exception{
        BillJSON billJSON = new BillJSON();

       /* SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat toFormat = new SimpleDateFormat("MMM dd, yyyy");*/

        JSONObject obj = new JSONObject(getIntent().getBundleExtra("jsonObject").getString("jsonObject"));

        billJSON.setBillId(obj.getString("bill_id"));
        billJSON.setBillType(obj.getString("bill_type"));
        billJSON.setChamber(obj.getString("chamber"));
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.text.SimpleDateFormat toFormat = new java.text.SimpleDateFormat("MMM dd, yyyy");
        billJSON.setIntroducedOn(toFormat.format(format.parse( obj.getString("introduced_on") )));
        //billJSON.setIntroducedOn((obj.getString("introduced_on")));
        JSONObject sponsor = obj.getJSONObject("sponsor");
        billJSON.setSponsor(sponsor.getString("title") + "," + sponsor.getString("last_name") + "," + sponsor.getString("first_name"));
        billJSON.setCongressURL(obj.getJSONObject("urls").getString("congress"));
        JSONObject url1 =(obj.getJSONObject("last_version"));
        JSONObject url2=url1.getJSONObject("urls");
        billJSON.setBillURL(url2.getString("pdf"));

        JSONObject status = obj.getJSONObject("history");
        if(status.getString("active") == "true")
            billJSON.setStatus("Active");
        else billJSON.setStatus("New");
        billJSON.setTitle(obj.getString("short_title"));

        JSONObject version = obj.getJSONObject("last_version");
        billJSON.setVersion(version.getString("version_name"));

        return billJSON;
    }



}
